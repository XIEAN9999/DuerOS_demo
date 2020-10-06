package priv.xiean.DuerOS_dock_demo.enums;

public enum TaskTypeEnum {

	GUIDE("guide"), CANCLE("cancle");
	private String name;

	TaskTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
