package priv.xiean.DuerOS_dock_demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import priv.xiean.DuerOS_dock_demo.model.DuVoiceBox;

/**
 * 
 * @description: DuVoiceBox的dao层接口
 * @author: xiean99
 * @date: 2020年9月4日 下午11:18:22
 */
@Repository
public interface DuVoiceBoxRepository {

	@Insert("insert into du_voice_box(id,place_id,device_id) values(null,#{placeId},#{deviceId})")
	public void insert(DuVoiceBox box);

	@Update("update du_voice_box set place_id=#{placeId} where device_id=#{deviceId}")
	public void updatePlaceId(@Param("deviceId") String deviceId, @Param("placeId") String placeId);

	@Select("select * from du_voice_box where device_id=#{deviceId}")
	public DuVoiceBox getboxByDevicedId(@Param("deviceId") String deviceId);

	@Select("select * from du_voice_box where place_id=#{placeId}")
	public List<DuVoiceBox> getboxByPlaceId(@Param("placeId") String placeId);

	@Delete("delete from du_voice_box where device_id=#{deviceId}")
	public void deleteBoxByDevicedId(@Param("deviceId") String deviceId);
}
