package priv.xiean.DuerOS_dock_demo.model;

import java.util.Date;


import net.sf.json.JSONObject;
import org.springframework.util.MultiValueMap;

/**
 * 
 * @description: 记录机器人调度任务结果
 * @author: xiean99
 * @date: 2020年9月4日 下午10:02:18
 */
public class RobotTask {

	private String id;
	private String placeId;
	private String deviceId;
	private String productId;
	private String type;
	private String target;
	private String taskId;
	private int errorCode;
	private String errorMsg;
	private Date createTime;

	public void fieldsSetter(MultiValueMap<String, String> params, JSONObject json) {
		deviceId = params.getFirst("deviceId");
		placeId = params.getFirst("placeId");
		productId = params.getFirst("productId");
		target = params.getFirst("target");
		type = params.getFirst("type");

		this.errorCode = json.getInt("errcode");
		if (errorCode == 0) {
			taskId = json.getJSONObject("data").getString("taskId");
		} else {
			errorMsg = json.getString("errmsg");
		}

	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
