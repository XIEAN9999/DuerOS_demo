package priv.xiean.DuerOS_dock_demo.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.dueros.certificate.Certificate;

import priv.xiean.DuerOS_dock_demo.bot.GuideBot;
import priv.xiean.DuerOS_dock_demo.robot.RobotServiceCall;
import priv.xiean.DuerOS_dock_demo.robot.RobotStatusQuery;

/**
 * 
 * @description: 对DuerOS提供引导技能的调用接口
 * @author: xiean99
 * @date: 2020年9月2日 下午7:40:23
 */
@RestController
@RequestMapping("/guide")
public class GuideController {
	@Autowired
	private RobotServiceCall robotServiceCall;
	@Autowired
	private RobotStatusQuery robotStatusQuery;

	/** 
	 * 客户引导技能对DuerOS提供的响应接口
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	@RequestMapping("/handler")
	public void handler(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Certificate certificate = new Certificate(request);
		if (!certificate.getMessage().equals("")) {
			GuideBot guideBot = new GuideBot(certificate, robotServiceCall);
			guideBot.enableVerify();
			String responseJson = guideBot.run();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append(responseJson);
		}
	}

	/**
	 * 用于测试内网穿透是否成功
	 * 
	 * @return
	 */
	@RequestMapping("/hello")
	public String hello(HttpSession session) {
		if (session.getAttribute("apiAccessToken") != null)
			System.out.println(session.getAttribute("apiAccessToken"));
		else
			System.out.println("none");
		return "welecome xiean";
	}

}
