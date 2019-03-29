package bmatser.service.impl;
//package bmatser.service.impl;
//
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Service;
//
//import bmatser.model.InterfaceLog;
//import bmatser.model.JTLInterfaceLogInfo;
//import bmatser.service.InterfaceLogI;
//@Service("InterfaceLog")
//public class InterfaceLogImpl implements InterfaceLogI{
//	@Autowired
//	private MongoTemplate mongoTemplate;
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
//	@Override
//	public void saveLogToMongoDB(String interfaceName,String interfaceUrl,String interfaceMethod,
//			String interfaceStr, String reSys, String desSys, String returnFlg, String returnMsg) {
//		try {
//			this.saveLogToMongoDB(interfaceName, interfaceUrl, interfaceMethod, interfaceStr, reSys, desSys, returnFlg, returnMsg, "");
///*			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(new Date());
//			//+8小时
//			cal.add(Calendar.HOUR_OF_DAY, 8);
//			Timestamp t = Timestamp.valueOf(sdf.format(cal.getTime()));
//			InterfaceLog i=new InterfaceLog();
//			i.setUseTime(t);
//			i.setDesSys(desSys);
//			i.setInterfaceMethod(interfaceMethod);
//			i.setInterfaceName(interfaceName);
//			i.setInterfaceStr(interfaceStr);
//			i.setInterfaceUrl(interfaceUrl);
//			i.setReSys(reSys);
//			i.setReturnFlg(returnFlg);
//			i.setReturnMsg(returnMsg);
//			mongoTemplate.save(i);*/
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
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
//	@Override
//	public void saveLogToMongoDB(String interfaceName,String interfaceUrl,String interfaceMethod,
//			String interfaceStr, String reSys, String desSys, String returnFlg, String returnMsg,String orderId) {
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(new Date());
//			//+8小时
//			cal.add(Calendar.HOUR_OF_DAY, 8);
//			Timestamp t = Timestamp.valueOf(sdf.format(cal.getTime()));
//			InterfaceLog i=new InterfaceLog();
//			i.setUseTime(t);
//			i.setDesSys(desSys);
//			i.setInterfaceMethod(interfaceMethod);
//			i.setInterfaceName(interfaceName);
//			i.setInterfaceStr(interfaceStr);
//			i.setInterfaceUrl(interfaceUrl);
//			i.setReSys(reSys);
//			i.setReturnFlg(returnFlg);
//			i.setReturnMsg(returnMsg);
//			i.setOrderId(orderId);
//			mongoTemplate.save(i);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	@Override
//	public void saveJTLLogInfo(JTLInterfaceLogInfo info) {
//		mongoTemplate.save(info, "jtlInterfaceLogInfo");
//	}
//	
//	
//
//}
