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

import jdk.internal.jline.internal.Log;
import net.sf.json.JSONObject;
import priv.xiean.DuerOS_dock_demo.util.DeviceInfoUtil;
import priv.xiean.DuerOS_dock_demo.util.SignUtil;

@Component
public class RobotServiceCall {

	@Value("${yunji.property.appname}")
	private String appname;
	@Value("${yunji.property.secert}")
	private String secret;
	@Value("${yunji.property.place_id}")
	private String SCHEDULED_ROBOT_PLACEID;
	@Value("${yunji.property.robot_id}")
	private String DEFAULT_ROBOT_ID;
	@Value("${yunji.api.specific_robot}")
	private String SPECIFIC_ROBORT_CALL_API;
	@Value("${yunji.api.scheduled_robot}")
	private String SCHEDULED_ROBORT_CALL_API;
	@Value("${yunji.api.robot_status_query}")
	private String ROBOT_STATUS_QUERY_API;
	
	@Autowired
	private DeviceInfoUtil deviceInfoUtil;

	/**
	 * 由指定机器人进行guide工作
	 * 
	 * @param params 调用API参数
	 */
	public void guideBySpecificRobot(MultiValueMap<String, String> params) {
		params.add("type", "guide");
		if (params.get("productId") == null) {
			params.add("productId", DEFAULT_ROBOT_ID);
		}
		generalCall(params, SPECIFIC_ROBORT_CALL_API);

	}

	/**
	 * 由系统调度的机器人进行guide工作
	 * 
	 * @param params
	 */
	public void guideByScheduledRobot(MultiValueMap<String, String> params) {
		params.add("type", "guide");
		params.add("placeId", SCHEDULED_ROBOT_PLACEID);
		generalCall(params, SCHEDULED_ROBORT_CALL_API);
	}

	public void guideByNearestRobot(MultiValueMap<String, String> params) {
		String apiAccessToken = params.getFirst("apiAccessionToken");
		if(apiAccessToken!=null) {
			System.out.println(deviceInfoUtil.getLocationInfo(apiAccessToken));
		}else {
			Log.info("还未取得用户授权");;
		}
	}
	/**
	 * 通用服务
	 * 
	 * @param params
	 * @param url    不同服务的具体的API
	 */
	private String generalCall(MultiValueMap<String, String> params, String url) {

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
		System.out.println(resultString);
		if (result.getInt("errcode") != 0) {
			// 出错处理
			System.out.println("---机器人调用失败--");
		} else {
			System.out.println("---机器人调用成功---");
		}
		return resultString;
	}

}
