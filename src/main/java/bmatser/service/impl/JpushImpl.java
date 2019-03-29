package bmatser.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

import com.alibaba.fastjson.JSONObject;

import bmatser.pageModel.JpushPage;
import bmatser.service.JpushI;
@Service("jpushService")
public class JpushImpl implements JpushI {

	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	@Override
	public void sendMessage(JpushPage jpushPage) throws Exception {
		String masterSecret = "41c7c69964aebd70cadb1054";
		String appKey = "02fbf4a5d91ee1c656bbb837";
		JPushClient client = new JPushClient(masterSecret, appKey);
		Builder builder = PushPayload.newBuilder().setPlatform(Platform.all());//推送设备 "android", "ios", "winphone"
		if(StringUtils.isNotBlank(jpushPage.getTag()) || StringUtils.isNotBlank(jpushPage.getTagAnd()) || StringUtils.isNotBlank(jpushPage.getAlias()) || StringUtils.isNotBlank(jpushPage.getRegistrationId())){
				builder.setAudience(Audience.tag(StringUtils.isNotBlank(jpushPage.getTag())?jpushPage.getTag().split(",") : null) 
																	.tag_and(StringUtils.isNotBlank(jpushPage.getTagAnd())?jpushPage.getTagAnd().split(",") : null)
																	.registrationId(StringUtils.isNotBlank(jpushPage.getRegistrationId())?jpushPage.getRegistrationId().split(",") : null)
																	.alias(StringUtils.isNotBlank(jpushPage.getAlias())?jpushPage.getAlias().split(",") : null));
		}else{
			builder.setAudience(Audience.all());
		}
		if(StringUtils.isNotBlank(jpushPage.getAlert())){
				Map jo = StringUtils.isNotBlank(jpushPage.getNotificationExtras())?JSONObject.parseObject(jpushPage.getNotificationExtras()) : null;
				builder.setNotification(Notification.android(jpushPage.getAlert(), jpushPage.getAndroidTitle(), jo)
																			  .ios(jpushPage.getAlert(), jo));
				
		}
		if(StringUtils.isNotBlank(jpushPage.getMsgContent())){
			builder.setMessage(Message.content(jpushPage.getMsgContent()));
		}
		PushPayload payload = builder.build();
		try {
			PushResult result = client.sendPush(payload);
			System.out.println(result.msg_id);
			System.out.println(result.sendno);
		} catch (APIConnectionException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (APIRequestException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

}
