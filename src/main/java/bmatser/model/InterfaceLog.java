package bmatser.model;

import java.sql.Timestamp;
/**
 * 调用外部接口保存到mongedb记录日志实体
 * @author felx  20160612
 *
 */
public class InterfaceLog {
	
	private String interfaceName;  //接口名(此接口是做什么操作)

	private String interfaceUrl;  //     接口路径

	private String interfaceMethod; //  接口方法(方法名)

	private String interfaceStr;   //    接口传送的完整字符(传过来的参数)

	private String reSys;         //       源系统

	private String desSys;        //     目标系统

	private Timestamp useTime;      //    调用时间

	private String returnFlg;     //    返回标识（0:成功，1：失败）

	private String returnMsg;    //   返回信息
	
	private String orderId ="";    //   返回信息

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfaceUrl() {
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}

	public String getInterfaceMethod() {
		return interfaceMethod;
	}

	public void setInterfaceMethod(String interfaceMethod) {
		this.interfaceMethod = interfaceMethod;
	}

	public String getInterfaceStr() {
		return interfaceStr;
	}

	public void setInterfaceStr(String interfaceStr) {
		this.interfaceStr = interfaceStr;
	}

	public String getReSys() {
		return reSys;
	}

	public void setReSys(String reSys) {
		this.reSys = reSys;
	}

	public String getDesSys() {
		return desSys;
	}

	public void setDesSys(String desSys) {
		this.desSys = desSys;
	}

	public Timestamp getUseTime() {
		return useTime;
	}

	public void setUseTime(Timestamp useTime) {
		this.useTime = useTime;
	}

	public String getReturnFlg() {
		return returnFlg;
	}

	public void setReturnFlg(String returnFlg) {
		this.returnFlg = returnFlg;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	

}
