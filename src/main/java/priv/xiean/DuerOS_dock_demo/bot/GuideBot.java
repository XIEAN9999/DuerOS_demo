package priv.xiean.DuerOS_dock_demo.bot;

import java.io.IOException;

import javax.servlet.http.HttpSession;

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
	private String criticalInfoMissingMsg = "请说明房间号";
	private String intentCancledMsg = "已经取消引导至%s房间";
	private String intentUnrecognized = "未能识别您的意图，请再说一遍";
	private String serviceEndedMsg = "欢迎再次使用引领服务";
	private String roomSlotName = "ROOM_NUM";
	private String roomSessionName = "ROOM_NUM";
	private String productIDSessionName = "PRODUCT_NAME";
	private String deviceIDSessionName = "DEVICE_ID";
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
				// 参数设置
				MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
				String deviceId = this.getDeviceId();
				params.add("target", room);
				params.add("deviceId", deviceId);
				System.out.println("got device ID:" + this.getDeviceId());
				// 调用云迹robot服务
				RobotServiceResultEnum result = robotServiceCall.guideBySpecificRobot(params);
				// 返回语音文字结果
				String msg = result.equals(RobotServiceResultEnum.SUCCESS) ? String.format(result.getMsg(), room)
						: result.getMsg();
				textCard.setContent(msg);
				outputSpeech.setText(msg);
				// 保存相关信息到session
				this.setSessionAttribute(roomSessionName, room);
				this.setSessionAttribute(productIDSessionName, result.getProductId());
				this.setSessionAttribute(deviceIDSessionName, deviceId);

				// 结束会话
				// this.endDialog();

				System.out.println("引导任务已启动:" + room + "\nid:" + intentRequest.getRequestId());
			} else {
				// 设置需要确认的槽位
				this.setConfirmSlot(this.roomSlotName);
				// 设置缺失关键词返回信息
				textCard.setContent(this.criticalInfoMissingMsg);
				outputSpeech.setText(this.criticalInfoMissingMsg);
				this.setExpectSpeech(true);

				System.out.println("房间号信息缺失");
			}
		} else if (intent.equals(this.cancleIntentName)
				&& ((room = this.getSessionAttribute(roomSessionName)) != null)) {
			//设置参数
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.set("productId", this.getSessionAttribute(productIDSessionName));
			params.set("deviceId", this.getSessionAttribute(deviceIDSessionName));
			params.set("back", "true");
			// 调用云迹robot服务
			Thread thread=new Thread(new Runnable() {
				@Override
				public void run() {
					 robotServiceCall.taskCancle(params);
				}
			 });
			thread.start();
			//RobotServiceResultEnum result = robotServiceCall.taskCancle(params);
			// 返回语音文字结果
			textCard.setContent(String.format(intentCancledMsg, room));
			outputSpeech.setText(String.format(intentCancledMsg, room));
			// 结束会话
			this.clearSessionAttribute();

			System.out.println("取消任务：" + this.getSessionAttribute(roomSessionName) + " ;"
					+ this.getSessionAttribute(productIDSessionName));
		} else {
			// 设置无法识别消息
			textCard.setContent(intentUnrecognized);
			outputSpeech.setText(intentUnrecognized);

			System.out.println("无法识别的意图:" + intent + intentRequest.getQuery().getOriginal());
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
		OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, serviceEndedMsg);
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
