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
	private String confirmIntentName = "ClientGuideConfirm";
	private String cancleIntentName = "ClientGuideCancle";
	private String needConfirmMsg = "�Ƿ�����������";
	private String criticalInfoMissingMsg = "��˵�������";
	private String intentConfirmedMsg = "���������ͻ�����";
	private String intentCancledMsg = "�Ѿ�ȡ����������";
	private String intentUnrecognized = "�޷�ʶ�����ͼ";
	private String roomSlotName = "ROOM_NUM";
	private String roomSessionAttrName = "ROOM_NUM";
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
		System.out.println("���ܴ�");
		new Response(outputSpeech, textCard);
		return response;
	}



	/**
	 * ����ʶ�����ͼ����ʽ�汾
	 */
	@Override
	protected Response onInent(IntentRequest intentRequest) {
		String intent = intentRequest.getIntentName();
		TextCard textCard = new TextCard();
		OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, null);

		if (intent.equals(this.guideIntentName)) {
			room = getSlot(this.roomSlotName);
			if (room != null) {
				// ����ȷ�Ϸ�����Ϣ
				textCard.setContent(this.needConfirmMsg + room);
				outputSpeech.setText(this.needConfirmMsg + room);
				// ���������Ϣ��session
				this.setSessionAttribute(roomSessionAttrName, room);

				System.out.println("��Ҫ����������:" + room + "���ȴ�ȷ��");
			} else {
				// ����ȱʧ�ؼ��ʷ�����Ϣ
				textCard.setContent(this.criticalInfoMissingMsg);
				outputSpeech.setText(this.criticalInfoMissingMsg);

				System.out.println("�������Ϣȱʧ");
			}
		} else if (intent.equals(this.confirmIntentName)
				&& ((room = this.getSessionAttribute(roomSessionAttrName)) != null)) {
			// ��������
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("target", room);
			params.add("deviceId", this.getRequest().getContext().getSystem().getDevice().getDeviceId());
			// ����robot����
			robotServiceCall.guideBySpecificRobot(params);
			// ���óɹ�������Ϣ
			textCard.setContent(this.intentConfirmedMsg + room);
			outputSpeech.setText(this.intentConfirmedMsg + room);
			// ���session�з����
			this.setSessionAttribute(roomSessionAttrName, null);

			System.out.println("����������ȷ��:" + room + "\nid:" + intentRequest.getRequestId());

		} else if (intent.equals(this.cancleIntentName) && ((room = this.getSessionAttribute(roomSessionAttrName)) != null)) {
			// ����ȡ����Ϣ
			textCard.setContent(this.intentCancledMsg + room);
			outputSpeech.setText(this.intentCancledMsg + room);
			// ���session�з����
			this.setSessionAttribute(roomSessionAttrName, null);

			System.out.println("����������ȡ��" + intentRequest.getRequestId());

		} else {
			// �����޷�ʶ����Ϣ
			textCard.setContent(this.intentUnrecognized);
			outputSpeech.setText(this.intentUnrecognized);

			System.out.println("�޷�ʶ�����ͼ:" + intent + " id:" + intentRequest.getRequestId());
		}
		Response response = new Response(outputSpeech, textCard);
		return response;
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
