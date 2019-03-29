package bmatser.service;
//package bmatser.service;
//
//import bmatser.model.JTLInterfaceLogInfo;
//
//public interface InterfaceLogI {
//	/**
//	 * //接口调用时保存日志到mongodb的InterfaceLog表
//	 * @param interfaceName 接口名(此接口是做什么操作)
//	 * @param interfaceUrl 接口路径
//	 * @param interfaceMethod 接口方法(方法名)
//	 * @param interfaceStr 接口传送的完整字符(传过来的参数)
//	 * @param reSys 源系统
//	 * @param desSys 目标系统
//	 * @param returnFlg 返回标识（0:成功，1：失败）
//	 * @param returnMsg 返回信息
//	 */
//	void saveLogToMongoDB(String interfaceName,String interfaceUrl,String interfaceMethod,
//			String interfaceStr, String reSys, String desSys, String returnFlg, String returnMsg);
//
//	void saveLogToMongoDB(String interfaceName, String interfaceUrl,
//			String interfaceMethod, String interfaceStr, String reSys,
//			String desSys, String returnFlg, String returnMsg, String orderId);
//	
//	void saveJTLLogInfo(JTLInterfaceLogInfo info);
//
//}
