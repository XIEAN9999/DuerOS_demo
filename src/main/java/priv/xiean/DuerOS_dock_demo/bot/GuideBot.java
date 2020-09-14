package priv.xiean.DuerOS_dock_demo.bot;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.baidu.dueros.bot.BaseBot;
import com.baidu.dueros.certificate.Certificate;
import com.baidu.dueros.data.request.IntentRequest;
import com.baidu.dueros.data.request.LaunchRequest;
import com.baidu.dueros.data.request.SessionEndedRequest;
import com.baidu.dueros.data.request.permission.event.PermissionGrantFailedEvent;
import com.baidu.dueros.data.request.permission.event.PermissionGrantedEvent;
import com.baidu.dueros.data.request.permission.event.PermissionRejectedEvent;
import com.baidu.dueros.data.response.OutputSpeech;
import com.baidu.dueros.data.response.OutputSpeech.SpeechType;
import com.baidu.dueros.data.response.card.TextCard;
import com.baidu.dueros.model.Response;
import com.fasterxml.jackson.databind.JsonMappingException;

import priv.xiean.DuerOS_dock_demo.enums.RobotServiceResultEnum;
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
	private String welecomeMsg = "欢迎使用客人引领服务";
	private String needConfirmMsg = "是否引导客人到%s房间";
	private String criticalInfoMissingMsg = "请说明房间号";
	private String intentConfirmedMsg = "即将引导客户到%s";
	private String intentCancledMsg = "已经取消引导至%s房间";
	private String intentUnrecognized="未能识别您的意图，请再说一遍";
	private String serviceEndedMsg = "欢迎再次使用引领服务";
	private String roomSlotName = "ROOM_NUM";
	private String roomSessionAttrName = "ROOM_NUM";
	private String room;
	private RobotServiceCall robotServiceCall;

	public GuideBot(Certificate certificate, RobotServiceCall robotServiceCall)
			throws JsonMappingException, IOException {
		super(certificate);
		super.setCertificate(certificate);
		this.robotServiceCall = robotServiceCall;
	}

	/**
	 * 处理技能启动事件
	 */
	@Override
	protected Response onLaunch(LaunchRequest launchRequest) {
		TextCard textCard = new TextCard(welecomeMsg);
		OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, welecomeMsg);
		Response response = new Response(outputSpeech, textCard);
		System.out.println("技能启动");
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
				textCard.setContent(String.format(needConfirmMsg, room));
				outputSpeech.setText(String.format(needConfirmMsg, room));
				// 保存相关信息到session
				this.setSessionAttribute(roomSessionAttrName, room);

				System.out.println("需要引导至房间:" + room + "，等待确认");
			} else {
				// 设置需要确认的槽位
				this.setConfirmSlot(this.roomSlotName);
				// 设置缺失关键词返回信息
				textCard.setContent(this.criticalInfoMissingMsg);
				outputSpeech.setText(this.criticalInfoMissingMsg);

				System.out.println("房间号信息缺失");
			}
		} else if (intent.equals(this.confirmIntentName)
				&& ((room = this.getSessionAttribute(roomSessionAttrName)) != null)) {
			// 参数设置
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			String devicedId = this.getDeviceId();
			params.add("target", room);
			params.add("deviceId", devicedId);
			System.out.println("got device ID:" + this.getDeviceId());
			// 调用robot服务
			RobotServiceResultEnum result = robotServiceCall.guideBySpecificRobot(params);
			// 返回结果处理
			textCard.setContent(result.getMsg());
			outputSpeech.setText(result.getMsg());
			// 清空session中房间号
			this.setSessionAttribute(roomSessionAttrName, null);
			// 结束会话
			this.endDialog();
			
			System.out.println("引导需求已确认:" + room + "\nid:" + intentRequest.getRequestId());

		} else if (intent.equals(this.cancleIntentName)
				&& ((room = this.getSessionAttribute(roomSessionAttrName)) != null)) {
			// 设置取消信息
			textCard.setContent(String.format(intentCancledMsg, room));
			outputSpeech.setText(String.format(intentCancledMsg, room));
			// 清空session中房间号
			this.setSessionAttribute(roomSessionAttrName, null);
			// 结束会话
			this.endDialog();
			
			System.out.println("引导需求已取消" + intentRequest.getRequestId());

		} else {
			// 设置无法识别消息
			textCard.setContent(intentUnrecognized);
			outputSpeech.setText(intentUnrecognized);

			System.out.println("无法识别的意图:" + intentRequest.getQuery().getOriginal());
		}

		Response response = new Response(outputSpeech, textCard);
		return response;
	}

	/**
	 * 处理会话关闭事件
	 */
	@Override
	protected Response onSessionEnded(SessionEndedRequest sessionEndRequest) {
		TextCard textCard = new TextCard(serviceEndedMsg);
		OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText,serviceEndedMsg);
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
