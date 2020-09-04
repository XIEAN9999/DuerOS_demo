package priv.xiean.DuerOS_dock_demo.bot;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.baidu.dueros.bot.BaseBot;
import com.baidu.dueros.data.request.IntentRequest;
import com.baidu.dueros.data.request.LaunchRequest;
import com.baidu.dueros.data.request.permission.event.PermissionGrantFailedEvent;
import com.baidu.dueros.data.request.permission.event.PermissionGrantedEvent;
import com.baidu.dueros.data.request.permission.event.PermissionRejectedEvent;
import com.baidu.dueros.data.response.OutputSpeech;
import com.baidu.dueros.data.response.OutputSpeech.SpeechType;
import com.baidu.dueros.data.response.card.TextCard;
import com.baidu.dueros.model.Response;

import priv.xiean.DuerOS_dock_demo.robot.RobotServiceCall;

/**
 * 
 * @description: 百度DuerOS技能响应类
 * @author: xiean99
 * @date: 2020年9月2日 下午7:39:29
 */
public class GuideBot extends BaseBot {

	private String guideIntentName = "ClientGuide";
	private String confirmIntentName = "ClientGuideConfirm";
	private String cancleIntentName = "ClientGuideCancle";
	private String needConfirmMsg = "是否引导至房间";
	private String criticalInfoMissingMsg = "请说明房间号";
	private String intentConfirmedMsg = "即将引导客户到：";
	private String intentCancledMsg = "已经取消引导至：";
	private String intentUnrecognized = "无法识别的意图";
	private String roomSlotName = "ROOM_NUM";
	private String roomSessionAttrName = "ROOM_NUM";
	private String room;
	private RobotServiceCall robotServiceCall;

	public GuideBot(HttpServletRequest request, RobotServiceCall robotServiceCall) throws IOException {
		super(request);
		this.robotServiceCall = robotServiceCall;
	}

	/**
	 * 处理技能启动事件
	 */
	@Override
	protected Response onLaunch(LaunchRequest launchRequest) {
		TextCard textCard = new TextCard("您好，欢迎使用引导服务");
		OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "您好，欢迎使用引导服务");
		System.out.println("技能打开");
		new Response(outputSpeech, textCard);
		return response;
	}

	/**
	 * 处理识别的意图
	 */
	@Override
	protected Response onInent(IntentRequest intentRequest) {
		String intent = intentRequest.getIntentName();
		TextCard textCard = new TextCard();
		OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, null);

		if (intent.equals(this.guideIntentName)) {
			room = getSlot(this.roomSlotName);
			if (room != null) {
				// 设置确认返回消息
				textCard.setContent(this.needConfirmMsg + room);
				outputSpeech.setText(this.needConfirmMsg + room);
				// 保存相关信息到session
				this.setSessionAttribute(roomSessionAttrName, room);

				System.out.println("需要引导至房间:" + room + "，等待确认");
			} else {
				// 设置缺失关键词返回信息
				textCard.setContent(this.criticalInfoMissingMsg);
				outputSpeech.setText(this.criticalInfoMissingMsg);

				System.out.println("房间号信息缺失");
			}
		} else if (intent.equals(this.confirmIntentName)
				&& ((room = this.getSessionAttribute(roomSessionAttrName)) != null)) {
			// 参数设置
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("target", room);
			params.add("deviceId", this.getRequest().getContext().getSystem().getDevice().getDeviceId());
			// 调用robot服务
			robotServiceCall.guideBySpecificRobot(params);
			// 设置成功返回消息
			textCard.setContent(this.intentConfirmedMsg + room);
			outputSpeech.setText(this.intentConfirmedMsg + room);
			// 清空session中房间号
			this.setSessionAttribute(roomSessionAttrName, null);

			System.out.println("引导需求已确认:" + room + "\nid:" + intentRequest.getRequestId());

		} else if (intent.equals(this.cancleIntentName)
				&& ((room = this.getSessionAttribute(roomSessionAttrName)) != null)) {
			// 设置取消信息
			textCard.setContent(this.intentCancledMsg + room);
			outputSpeech.setText(this.intentCancledMsg + room);
			// 清空session中房间号
			this.setSessionAttribute(roomSessionAttrName, null);

			System.out.println("引导需求已取消" + intentRequest.getRequestId());

		} else {
			// 设置无法识别消息
			textCard.setContent(this.intentUnrecognized);
			outputSpeech.setText(this.intentUnrecognized);

			System.out.println("无法识别的意图:" + intent + " id:" + intentRequest.getRequestId());
		}
		Response response = new Response(outputSpeech, textCard);
		return response;
	}

	/**
	 * 处理用户同意授权事件
	 */
	@Override
	protected Response onPermissionGrantedEvent(PermissionGrantedEvent permissionGrantedEvent) {
		return null;
	}

	/**
	 * 处理用户拒绝授权事件
	 */
	protected Response onPermissionRejectedEvent(PermissionRejectedEvent permissionRejectedEvent) {
		return null;
	}

	/**
	 * 处理授权失败事件
	 */
	protected Response onPermissionGrantFailedEvent(PermissionGrantFailedEvent permissionGrantFailedEvent) {
		return null;
	}

}
