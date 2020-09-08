package priv.xiean.DuerOS_dock_demo.service;

import java.util.List;

import priv.xiean.DuerOS_dock_demo.model.RobotTask;

/**
 * 
 * @description: RobotTask的service层接口
 * @author: xiean99
 * @date: 2020年9月4日 下午10:22:12
 */
public interface RobotTaskService {
	public void insert(RobotTask task);

	public List<RobotTask> getTasksByProductId(String id);

	public List<RobotTask> getTaskById(String id);

	public List<RobotTask> getTaskFailed();

	public List<RobotTask> getTasksAll();

	public String getPlaceIdByAddr();

}
