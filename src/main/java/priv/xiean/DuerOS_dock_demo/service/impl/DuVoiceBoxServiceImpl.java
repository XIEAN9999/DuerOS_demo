package priv.xiean.DuerOS_dock_demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import priv.xiean.DuerOS_dock_demo.model.DuVoiceBox;
import priv.xiean.DuerOS_dock_demo.service.DuVoiceBoxService;

/**
 * 
 * @description: DuVoiceBox的service层实现
 * @author: xiean99
 * @date: 2020年9月4日 下午11:29:39
 */
@Service
public class DuVoiceBoxServiceImpl implements DuVoiceBoxService {

	@Autowired
	private DuVoiceBoxService duVoiceBoxService;

	@Override
	public void insert(DuVoiceBox box) {
		duVoiceBoxService.insert(box);
	}

	@Override
	public void updatePlaceId(String deviceId, String placeId) {
		duVoiceBoxService.updatePlaceId(deviceId, placeId);
	}

	@Override
	public DuVoiceBox getboxByDevicedId(String deviceId) {
		return duVoiceBoxService.getboxByDevicedId(deviceId);
	}

	@Override
	public List<DuVoiceBox> getboxByPlaceId(String placeId) {
		return duVoiceBoxService.getboxByPlaceId(placeId);
	}

	@Override
	public void deleteBoxByDevicedId(String deviceId) {
		duVoiceBoxService.deleteBoxByDevicedId(deviceId);
	}
}
