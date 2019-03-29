package bmatser.pageModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xmlStr")
public class Model {
	private String ToUserName;
	
	private String CreateTime;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	

}
