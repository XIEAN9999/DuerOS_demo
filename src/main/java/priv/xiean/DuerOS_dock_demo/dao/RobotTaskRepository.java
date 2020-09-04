package priv.xiean.DuerOS_dock_demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import priv.xiean.DuerOS_dock_demo.model.RobotTask;

/**
 * 
 * @description: RobotTask的dao层接口
 * @author: xiean99
 * @date: 2020年9月4日 下午10:22:52
 */
@Repository
public interface RobotTaskRepository {

	@Insert("insert into robot_task(id,product_id,type,target,task_id,error_code,error_msg) "
			+ "values(null,#{productId},#{type},#{target},#{taskId},#{errorCode},#{errorMsg})")
	public void insert(RobotTask task);

	@Update("update robot_task set task_id=#{taskId},error_code=#{errorCode},error_msg=#{errorMsg} where id=#{id}")
	public void updateTaskResult(@Param("taskId") String taskId, @Param("errorCode") String errorCode,
			@Param("errorMsg") String errorMsg, @Param("id") String id);

	@Select("select * from robot_task where product_id=#{productId}")
	public List<RobotTask> getTasksByProductId(@Param("productId") String id);

	@Select("select * from robot_task where error_code!=0")
	public List<RobotTask> getTaskFailed();

	@Select("select * from robot_task")
	public List<RobotTask> getTasksAll();

	@Select("select * from robot_task where task_id=#{taskId}")
	public List<RobotTask> getTaskById(@Param("taskId") String taskId);
}
