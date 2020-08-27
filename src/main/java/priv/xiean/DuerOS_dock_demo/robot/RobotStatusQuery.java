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
	 * ��ѯָ��������״̬
	 * 
	 * @param productId ������id
	 * @return ���ý��
	 */
	public String robotStatusQuery(String productId) {
		Map<String, String> params = new HashMap<String, String>();
		productId = productId == null ? DEFAULT_ROBOT_ID : productId;
		params.put("productId", productId);
		return generalCall(params, ROBOT_STATUS_QUERY_API);
	}

	/**
	 * ��ѯָ�������˵�����״̬
	 * 
	 * @param productId ������id
	 * @return ���ý��
	 */
	public String taskStatusQuery(String productId) {
		Map<String, String> params = new HashMap<String, String>();
		productId = productId == null ? DEFAULT_ROBOT_ID : productId;
		params.put("productId", productId);
		return generalCall(params, TAKS_STATUS_QUERY_API);
	}

	/**
	 * ͨ�÷���
	 * 
	 * @param params
	 * @param url    ��ͬ����ľ����API
	 */
	private String generalCall(Map<String, String> params, String url) {

		// ����׼��
		long ts = System.currentTimeMillis();
		String sign = SignUtil.getSign(params, appname, secret, ts);
		params.put("appname", appname);
		params.put("ts", Long.toString(ts));
		params.put("sign", sign);

		// ��ӡ������Ϣ
		String requestParams = "?";
		for (String key : params.keySet()) {
			requestParams += key + "={" + key + "}&";
		}
		System.out.println("������������:" + url + requestParams);

		// GET����������API
		HttpHeaders header = new HttpHeaders();
		HttpEntity<String> requestEntity = new HttpEntity<>(header);
		RestTemplate client = new RestTemplate();
		ResponseEntity<String> response = client.exchange(url + requestParams, HttpMethod.GET, requestEntity,
				String.class, params);

		// ����������
		String resultString = response.getBody();
		JSONObject result = JSONObject.fromObject(resultString);
		System.out.println(resultString);
		if (result.getInt("errcode") != 0) {
			// ������
			System.out.println("---�����˵���ʧ��- - -");
		} else {
			System.out.println("---�����˵��óɹ�- - -");
		}
		return resultString;
	}
}
