package priv.xiean.DuerOS_dock_demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DeviceInfoUtil {

	@Value("${deuros.api.device.location}")
	private String DEVICE_LOCATION_GET_API;

	public String getLocationInfo(String apiAccessToken) {
		HttpHeaders header = new HttpHeaders();
		header.add("authorization", "Bearer " + apiAccessToken);
		HttpEntity<String> requestEntity = new HttpEntity<>(header);
		RestTemplate client = new RestTemplate();
		ResponseEntity<String> response = client.exchange(DEVICE_LOCATION_GET_API, HttpMethod.GET, requestEntity,
				String.class);
		return response.getBody();
	}
}
