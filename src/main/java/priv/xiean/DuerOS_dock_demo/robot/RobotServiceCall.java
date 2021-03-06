package priv.xiean.DuerOS_dock_demo.robot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONObject;
import priv.xiean.DuerOS_dock_demo.enums.RobotServiceResultEnum;
import priv.xiean.DuerOS_dock_demo.model.DuVoiceBox;
import priv.xiean.DuerOS_dock_demo.model.RobotTask;
import priv.xiean.DuerOS_dock_demo.service.DuVoiceBoxService;
import priv.xiean.DuerOS_dock_demo.service.RobotTaskService;
import priv.xiean.DuerOS_dock_demo.util.DeviceInfoUtil;
import priv.xiean.DuerOS_dock_demo.util.SignUtil;

/**
 * @description: 用于调用云迹机器人服务API
 * @author: xiean99
 * @date: 2020年9月2日 下午7:38:09
 */
@Component
public class RobotServiceCall {

	@Value("${yunji.property.appname}")
	private String appname;
	@Value("${yunji.property.secert}")
	private String secret;
	@Value("${yunji.property.robot_id}")
	private String DEFAULT_ROBOT_ID;
	@Value("${yunji.api.specific_robot}")
	private String SPECIFIC_ROBORT_CALL_API;
	@Value("${yunji.api.scheduled_robot}")
	private String SCHEDULED_ROBORT_CALL_API;
	@Value("${yunji.api.robot_status_query}")
	private String ROBOT_STATUS_QUERY_API;
	@Value("${yunji.api.task_cancle}")
	private String TASK_CANCLE_API;

	@Autowired
	private DeviceInfoUtil deviceInfoUtil;
	@Autowired
	private RobotTaskService robotTaskService;
	@Autowired
	private DuVoiceBoxService duVoiceBoxService;

	/**
	 * 由指定机器人进行guide工作
	 * 
	 * @param params 调用API参数
	 */
	public RobotServiceResultEnum guideBySpecificRobot(MultiValueMap<String, String> params) {
		DuVoiceBox box = duVoiceBoxService.getBoxByDevicedId(params.getFirst("deviceId"));
		if (box == null) {
			return RobotServiceResultEnum.DEVICE_NOT_MAPPED;
		}
		params.add("type", "guide");
		params.add("apiType", "specific");
		params.add("productId", box.getProductId());
		generalCall(params, SPECIFIC_ROBORT_CALL_API);
		RobotServiceResultEnum res = RobotServiceResultEnum.SUCCESS;
		res.setProductId(box.getProductId());
		return res;
	}

	/**
	 * 由系统调度的机器人,在指定的地点进行guide工作
	 * 
	 * @param params 调用API参数
	 */
	public RobotServiceResultEnum guideByScheduledRobot(MultiValueMap<String, String> params) {
		params.add("type", "guide");
		params.add("apiType", "schedule");
		String placeId = robotTaskService.getPlaceIdByAddr(params.getFirst("addr"));
		if (placeId == null) {
			return RobotServiceResultEnum.NO_SUCH_PLACE;
		}
		params.add("placeId", placeId);
		generalCall(params, SCHEDULED_ROBORT_CALL_API);
		return RobotServiceResultEnum.SUCCESS;
	}

	/**
	 * 强制取消机器人任务
	 * 
	 * @param params 调用API参数
	 */
	public RobotServiceResultEnum taskCancle(MultiValueMap<String, String> params) {
		params.add("type", "cancle");
		return generalCall(params, TASK_CANCLE_API).equals(RobotServiceResultEnum.SUCCESS)
				? RobotServiceResultEnum.CANCLE_SUCCESSFULLY
				: RobotServiceResultEnum.CANCLE_FAILED;
	}

	/**
	 * 通用服务
	 * 
	 * @param params 调用API参数
	 * @param url    不同服务的具体的API
	 */
	private RobotServiceResultEnum generalCall(MultiValueMap<String, String> params, String url) {

		// 参数准备
		long ts = System.currentTimeMillis();
		String sign = SignUtil.getSign(params, appname, secret, ts);
		params.add("appname", appname);
		params.add("ts", Long.toString(ts));
		params.add("sign", sign);

		// 打印调试信息
		System.out.println("即将返回数据:");
		params.entrySet().stream().forEach(item -> System.out.println(item.getKey() + " " + item.getValue().get(0)));

		// POST请求调用相关API
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params,
				header);
		RestTemplate client = new RestTemplate();
		ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);

		// 请求结果处理
		String resultString = response.getBody();
		JSONObject result = JSONObject.fromObject(resultString);
		// 将结果新增至数据库
		RobotTask task = new RobotTask();
		task.fieldsSetter(params, result);
		robotTaskService.insert(task);

		System.out.println(resultString);
		if (result.getInt("errcode") != 0) {
			// 出错处理
			System.out.println("---机器人调用失败---");
			return RobotServiceResultEnum.UNDEFINE;
		} else {
			System.out.println("---机器人调用成功---");
			return RobotServiceResultEnum.SUCCESS;
		}
	}

}
