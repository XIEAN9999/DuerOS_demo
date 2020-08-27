package priv.xiean.DuerOS_dock_demo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import priv.xiean.DuerOS_dock_demo.bot.GuideBot;
import priv.xiean.DuerOS_dock_demo.robot.RobotServiceCall;
import priv.xiean.DuerOS_dock_demo.robot.RobotStatusQuery;

@RestController
@RequestMapping("/guide")
public class GuideController {
	@Autowired
	private RobotServiceCall robotServiceCall;
	@Autowired
	private RobotStatusQuery robotStatusQuery;

	/**
	 * �ͻ��������ܶ�DuerOS�ṩ����Ӧ�ӿ�
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	@RequestMapping("/response")
	public void handler(HttpServletRequest request, HttpServletResponse response) throws Exception {

		GuideBot guideBot = new GuideBot(request, robotServiceCall);
		guideBot.disableVerify();
		String responseJson = guideBot.run();
		response.setCharacterEncoding("UTF-8");
		response.getWriter().append(responseJson);
	}

	/**
	 * ���ڲ���������͸�Ƿ�ɹ�
	 * 
	 * @return
	 */
	@RequestMapping("/hello")
	public String hello() {
		return "welecome xiean";
	}

}
