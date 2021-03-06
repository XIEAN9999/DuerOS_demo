package priv.xiean.DuerOS_dock_demo.model;

import java.util.Date;

import net.sf.json.JSONObject;
import priv.xiean.DuerOS_dock_demo.enums.TaskTypeEnum;

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
	private String taskType;
	private String target;
	private String taskId;
	private int errorCode;
	private String errorMsg;
	private Date createTime;
	private String apiType;

	public void fieldsSetter(MultiValueMap<String, String> params, JSONObject json) {
		deviceId = params.getFirst("deviceId");
		placeId = params.getFirst("placeId");
		productId = params.getFirst("productId");
		target = params.getFirst("target");
		taskType = params.getFirst("type");
		apiType = params.getFirst("apiType");
		this.errorCode = json.getInt("errcode");
		if (errorCode == 0) {
			if(taskType.equals(TaskTypeEnum.GUIDE.getName())) {
				taskId = json.getJSONObject("data").getString("taskId");
			}
		} else {
			errorMsg = json.getString("errmsg");
		}

	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
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

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
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
