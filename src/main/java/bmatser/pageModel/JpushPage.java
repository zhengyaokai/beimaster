package bmatser.pageModel;

public class JpushPage {
	
	/** audience 观众 */
	private String tag;
	
	private String tagAnd;

	/** 别名,化名*/
	private String alias;
	
	private String registrationId;

	/** notification 通知 */
	
	private String alert;
	
	private String notificationExtras;
	/** android */
	private String androidTitle;
	
	private Integer builderId;
	/** ios  象征*/
	private String sound;
	
	private String badge;
	
	private String contentAvailable;
	
	private String category;
	
	/**message*/
	private String msgContent;
	
	private String messageTitle;
	
	private String content_type;
	
	private String messageExtras;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTagAnd() {
		return tagAnd;
	}

	public void setTagAnd(String tagAnd) {
		this.tagAnd = tagAnd;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getNotificationExtras() {
		return notificationExtras;
	}

	public void setNotificationExtras(String notificationExtras) {
		this.notificationExtras = notificationExtras;
	}

	public String getAndroidTitle() {
		return androidTitle;
	}

	public void setAndroidTitle(String androidTitle) {
		this.androidTitle = androidTitle;
	}

	public Integer getBuilderId() {
		return builderId;
	}

	public void setBuilderId(Integer builderId) {
		this.builderId = builderId;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getContentAvailable() {
		return contentAvailable;
	}

	public void setContentAvailable(String contentAvailable) {
		this.contentAvailable = contentAvailable;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getMessageExtras() {
		return messageExtras;
	}

	public void setMessageExtras(String messageExtras) {
		this.messageExtras = messageExtras;
	}
	
	
}
