package priv.xiean.DuerOS_dock_demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import priv.xiean.DuerOS_dock_demo.dao.RobotTaskRepository;
import priv.xiean.DuerOS_dock_demo.model.RobotTask;
import priv.xiean.DuerOS_dock_demo.service.RobotTaskService;

/**
 * 
 * @description: RobotTask的service层实现
 * @author: xiean99
 * @date: 2020年9月4日 下午10:23:13
 */
@Service
public class RobotTaskServiceImpl implements RobotTaskService{

	@Autowired
	private RobotTaskRepository robotTaskRepostory;

	@Override
	public void insert(RobotTask task) {
		robotTaskRepostory.insert(task);
	}

	@Override
	public List<RobotTask> getTasksByProductId(String id) {
		return robotTaskRepostory.getTasksByProductId(id);
	}

	@Override
	public List<RobotTask> getTaskById(String id) {
		return robotTaskRepostory.getTaskById(id);
	}

	@Override
	public List<RobotTask> getTaskFailed() {
		return robotTaskRepostory.getTaskFailed();
	}

	@Override
	public List<RobotTask> getTasksAll() {
		return robotTaskRepostory.getTasksAll();
	}

	@Override
	public String getPlaceIdByAddr() {
		return null;
	}
}
