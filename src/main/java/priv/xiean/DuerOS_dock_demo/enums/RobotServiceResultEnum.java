package priv.xiean.DuerOS_dock_demo.enums;

public enum RobotServiceResultEnum {

	SUCCESS(0, "正在调度机器人引导客人到%s房间"), 
	CANCLE_SUCCESSFULLY(1,"已经取消引导至%s房间"),
	DEVICE_NOT_MAPPED(-1, "当前设备还不支持该功能，如需此功能，请联系:0512-57171519"), 
	NO_DEVICE_SPCIFIED(-2, "调度机器人进行引导服务失败，未指明音箱设备"),
	NO_SUCH_PLACE(-3, "调度机器人进行引导服务失败，服务地点不存在"),
	UNDEFINE(-4, "调度机器人进行引导服务失败"),
	CANCLE_FAILED(-5,"取消引导失败");

	private int code;
	private String msg;
	private String productId;
	
	RobotServiceResultEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static String msg(int code) {
		for (RobotServiceResultEnum m : RobotServiceResultEnum.values()) {
			if (m.getCode() == code) {
				return m.getMsg();
			}
		}
		return UNDEFINE.getMsg();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}