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
import com.baidu.dueros.data.response.directive.permission.AskForPermissionsConsent;
import com.baidu.dueros.data.response.directive.permission.Permission;
import com.baidu.dueros.model.Response;

import priv.xiean.DuerOS_dock_demo.robot.RobotServiceCall;

public class GuideBot extends BaseBot {

	private String guideIntentName = "ClientGuide";
	private String confirmIntentName = "ai.dueros.common.confirm_intent";
	private String cancleIntentName = "ai.dueros.common.cancel_intent";
	private String roomSlotName = "ROOM_NUM";
	private String room;
	private RobotServiceCall robotServiceCall;

	public GuideBot(HttpServletRequest request, RobotServiceCall robotServiceCall) throws IOException {
		super(request);
		this.robotServiceCall = robotServiceCall;
	}

	/**
	 * �����������¼�
	 */
	@Override
	protected Response onLaunch(LaunchRequest launchRequest) {
		TextCard textCard = new TextCard("���ã���ӭʹ����������");
		OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "���ã���ӭʹ����������");

		if (this.getSessionAttribute("apiAccessToken") == null) {
			this.setSessionAttribute("apiAccessToken", this.getApiAccessToken());
			this.setSessionAttribute("apiEndpoint", this.getApiEndPoint());
			System.out.println("��ȡtoken\\endpoint");
			AskForPermissionsConsent permission = new AskForPermissionsConsent();
			permission.addPermission(Permission.READ_DEVICE_LOCATION);
			this.addDirective(permission);
		}
		Response response = new Response(outputSpeech, textCard);
		return response;
	}

	/**
	 * ����ʶ�����ͼ
	 */
	@Override
	protected Response onInent(IntentRequest intentRequest) {
		String intent = intentRequest.getIntentName();
		if (intent.equals(this.guideIntentName)) {
			room = getSlot(this.roomSlotName);
			if (room != null) {
				TextCard textCard = new TextCard("������������:" + room);
				Response response = new Response(null, textCard);
				System.out.println("��Ҫ����������:" + room);
				// ��������
				MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
				params.add("target", room);
				// ����robot����
				robotServiceCall.guideBySpecificRobot(params);
				return response;
			} else {
				TextCard textCard = new TextCard("��˵�������");
				// OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "");
				Response response = new Response(null, textCard);
				System.out.println("�������Ϣȱʧ");
				return response;

			}
		} else if (intent.equals(this.confirmIntentName)) {
			System.out.println("����������ȷ��");
		} else if (intent.equals(this.cancleIntentName)) {
			System.out.println("����������ȡ��");
		}

		return null;
	}

	/**
	 * �����û�ͬ����Ȩ�¼�
	 */
	@Override
	protected Response onPermissionGrantedEvent(PermissionGrantedEvent permissionGrantedEvent) {
		return null;
	}

	/**
	 * �����û��ܾ���Ȩ�¼�
	 */
	protected Response onPermissionRejectedEvent(PermissionRejectedEvent permissionRejectedEvent) {
		return null;
	}

	/**
	 * ������Ȩʧ���¼�
	 */
	protected Response onPermissionGrantFailedEvent(PermissionGrantFailedEvent permissionGrantFailedEvent) {
		return null;
	}

}
