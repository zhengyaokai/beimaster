package bmatser.text;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.PushPayload.Builder;

import cn.jpush.api.push.model.notification.Notification;

public class JiGuang {

	public static void main(String[] args) {
		push();
	}
	private static void push(){
		String masterSecret = "41c7c69964aebd70cadb1054";
		String appKey = "02fbf4a5d91ee1c656bbb837";
		JPushClient client = new JPushClient(masterSecret, appKey);
		System.out.println(Audience.all());
		Builder builder = PushPayload.newBuilder().setPlatform(Platform.all());//推送设备 "android", "ios", "winphone"
		
		PushPayload payload = PushPayload.newBuilder()
																		.setPlatform(Platform.all())//推送设备 "android", "ios", "winphone"
																		.setAudience(Audience.all())
																		.setNotification(Notification.alert("text code"))
																		.setMessage(Message.content("text code"))
																		.build();
		try {
			PushResult result = client.sendPush(payload);
			System.out.println(result.msg_id);
			System.out.println(result.sendno);
		} catch (APIConnectionException e) {
			System.out.println("连接错误");
			e.printStackTrace();
		} catch (APIRequestException e) {
			System.out.println("请求错误");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
