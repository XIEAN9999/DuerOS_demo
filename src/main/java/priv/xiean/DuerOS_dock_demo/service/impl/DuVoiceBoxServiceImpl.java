package priv.xiean.DuerOS_dock_demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import priv.xiean.DuerOS_dock_demo.dao.DuVoiceBoxRepository;
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
	private DuVoiceBoxRepository duVoiceBoxRepository;

	@Override
	public void insert(DuVoiceBox box) {
		duVoiceBoxRepository.insert(box);
	}
 
	@Override
	public void updateProductId(String deviceId, String productId) {
		duVoiceBoxRepository.updateProductId(deviceId, productId);
	}

	@Override
	public DuVoiceBox getBoxByDevicedId(String deviceId) {
		return duVoiceBoxRepository.getBoxByDevicedId(deviceId);
	}

	@Override
	public List<DuVoiceBox> getBoxByProductId(String productId) {
		return duVoiceBoxRepository.getBoxByProductId(productId);
	}

	@Override
	public void deleteBoxByDevicedId(String deviceId) {
		duVoiceBoxRepository.deleteBoxByDevicedId(deviceId);
	}
}
