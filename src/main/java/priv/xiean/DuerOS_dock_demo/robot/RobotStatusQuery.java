package priv.xiean.DuerOS_dock_demo.robot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONObject;
import priv.xiean.DuerOS_dock_demo.util.SignUtil;

@Component
public class RobotStatusQuery {

	@Value("${yunji.property.appname}")
	private String appname;
	@Value("${yunji.property.secert}")
	private String secret;
	@Value("${yunji.property.place_id}")
	private String SCHEDULED_ROBOT_PLACEID;
	@Value("${yunji.property.robot_id}")
	private String DEFAULT_ROBOT_ID;
	@Value("${yunji.api.robot_status_query}")
	private String ROBOT_STATUS_QUERY_API;
	@Value("${yunji.api.task_status_query}")
	private String TAKS_STATUS_QUERY_API;

	/**
	 * 查询指定机器人状态
	 * 
	 * @param productId 机器人id
	 * @return 调用结果
	 */
	public String robotStatusQuery(String productId) {
		Map<String, String> params = new HashMap<String, String>();
		productId = productId == null ? DEFAULT_ROBOT_ID : productId;
		params.put("productId", productId);
		return generalCall(params, ROBOT_STATUS_QUERY_API);
	}

	/**
	 * 查询指定机器人的任务状态
	 * 
	 * @param productId 机器人id
	 * @return 调用结果
	 */
	public String taskStatusQuery(String productId) {
		Map<String, String> params = new HashMap<String, String>();
		productId = productId == null ? DEFAULT_ROBOT_ID : productId;
		params.put("productId", productId);
		return generalCall(params, TAKS_STATUS_QUERY_API);
	}

	/**
	 * 通用服务
	 * 
	 * @param params
	 * @param url    不同服务的具体的API
	 */
	private String generalCall(Map<String, String> params, String url) {

		// 参数准备
		long ts = System.currentTimeMillis();
		String sign = SignUtil.getSign(params, appname, secret, ts);
		params.put("appname", appname);
		params.put("ts", Long.toString(ts));
		params.put("sign", sign);

		// 打印调试信息
		String requestParams = "?";
		for (String key : params.keySet()) {
			requestParams += key + "={" + key + "}&";
		}
		System.out.println("即将返回数据:" + url + requestParams);

		// GET请求调用相关API
		HttpHeaders header = new HttpHeaders();
		HttpEntity<String> requestEntity = new HttpEntity<>(header);
		RestTemplate client = new RestTemplate();
		ResponseEntity<String> response = client.exchange(url + requestParams, HttpMethod.GET, requestEntity,
				String.class, params);

		// 请求结果处理
		String resultString = response.getBody();
		JSONObject result = JSONObject.fromObject(resultString);
		System.out.println(resultString);
		if (result.getInt("errcode") != 0) {
			// 出错处理
			System.out.println("---机器人调用失败- - -");
		} else {
			System.out.println("---机器人调用成功- - -");
		}
		return resultString;
	}
}
