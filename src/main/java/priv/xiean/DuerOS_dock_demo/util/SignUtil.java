package priv.xiean.DuerOS_dock_demo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;

public class SignUtil {
	public static String getSign(MultiValueMap<String, String> params, String appname, String secret, long ts) {
		List<String> kvs = new ArrayList<>();
		for (MultiValueMap.Entry<String, List<String>> entry : params.entrySet()) {
			String key = StringUtils.trim(entry.getKey());
			String value = StringUtils.trim(entry.getValue().get(0));
			if (key.equalsIgnoreCase("appname") || key.equalsIgnoreCase("secret") || key.equalsIgnoreCase("ts")
					|| key.equalsIgnoreCase("sign") || StringUtils.isBlank(value))
				continue;
			kvs.add(key + ":" + value);
		}
		Collections.sort(kvs);
		kvs.add("appname:" + appname);
		kvs.add("secret:" + secret);
		kvs.add("ts:" + ts);
		return DigestUtils.md5DigestAsHex(StringUtils.join(kvs, "|").getBytes());
	}

	public static String getSign(Map<String, String> params, String appname, String secret, long ts) {
		List<String> kvs = new ArrayList<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = StringUtils.trim(entry.getKey());
			String value = StringUtils.trim(entry.getValue());
			if (key.equalsIgnoreCase("appname") || key.equalsIgnoreCase("secret") || key.equalsIgnoreCase("ts")
					|| key.equalsIgnoreCase("sign") || StringUtils.isBlank(value))
				continue;
			kvs.add(key + ":" + value);
		}
		Collections.sort(kvs);
		kvs.add("appname:" + appname);
		kvs.add("secret:" + secret);
		kvs.add("ts:" + ts);
		return DigestUtils.md5DigestAsHex(StringUtils.join(kvs, "|").getBytes());
	}

}