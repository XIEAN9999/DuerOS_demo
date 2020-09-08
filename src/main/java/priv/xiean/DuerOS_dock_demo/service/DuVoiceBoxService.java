package priv.xiean.DuerOS_dock_demo.service;

import java.util.List;

import priv.xiean.DuerOS_dock_demo.model.DuVoiceBox;

/**
 * 
 * @description: DuVoiceBox的service层接口
 * @author: xiean99
 * @date: 2020年9月4日 下午11:27:30
 */
public interface DuVoiceBoxService {
	public void insert(DuVoiceBox box);

	public void updateProductId(String deviceId, String productId);

	public DuVoiceBox getBoxByDevicedId(String deviceId);

	public List<DuVoiceBox> getBoxByProductId(String productId);

	public void deleteBoxByDevicedId(String deviceId);
}
