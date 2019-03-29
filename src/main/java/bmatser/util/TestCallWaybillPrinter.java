package bmatser.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.dto.CargoInfoDto;
import com.sf.dto.WaybillDto;
import com.sf.util.Base64ImageTools;



public class TestCallWaybillPrinter {
	
	public static void main(String [] args) throws Exception{
		TestCallWaybillPrinter.WayBillPrinterTools();

	
	}

	
	
	public static void WayBillPrinterTools() throws Exception{
		
        
		/*********2联单**************/
		/**
		 * 调用打印机 不弹出窗口 适用于批量打印【二联单】
		 */
		String url1 = "http://localhost:4040/sf/waybill/print?type=V2.0_poster_100mm150mm&output=noAlertPrint";
		/**
		 * 调用打印机 弹出窗口 可选择份数 适用于单张打印【二联单】
		 */
		String url2 = "http://localhost:4040/sf/waybill/print?type=V2.0_poster_100mm150mm&output=print";

		/**
		 * 直接输出图片的BASE64编码字符串 可以使用html标签直接转换成图片【二联单】
		 */
		String url3 = "http://localhost:4040/sf/waybill/print?type=V2.0_poster_100mm150mm&output=image";
        
		
		/*********3联单**************/
		/**
		 * 调用打印机 不弹出窗口 适用于批量打印【三联单】
		 */
		String url4 = "http://localhost:4040/sf/waybill/print?type=V3.0_poster_100mm210mm&output=noAlertPrint";
		/**
		 * 调用打印机 弹出窗口 可选择份数 适用于单张打印【三联单】
		 */
		String url5 = "http://localhost:4040/sf/waybill/print?type=V3.0_poster_100mm210mm&output=print";

		/**
		 * 直接输出图片的BASE64编码字符串 可以使用html标签直接转换成图片【三联单】
		 */
		String url6 = "http://localhost:4040/sf/waybill/print?type=V3.0_poster_100mm210mm&output=image";
       
		
		/*********2联150 丰密运单**************/
		/**
		 * 调用打印机 不弹出窗口 适用于批量打印【二联单】
		 */
		String url7 = "http://localhost:4040/sf/waybill/print?type=V2.0.FM_poster_100mm150mm&output=noAlertPrint";
		/**
		 * 调用打印机 弹出窗口 可选择份数 适用于单张打印【二联单】
		 */
		String url8 = "http://localhost:4040/sf/waybill/print?type=V2.0.FM_poster_100mm150mm&output=print";

		/**
		 * 直接输出图片的BASE64编码字符串 可以使用html标签直接转换成图片【二联单】
		 */
		String url9 = "http://localhost:4040/sf/waybill/print?type=V2.0.FM_poster_100mm150mm&output=image";
		
		
		/*********3联210 丰密运单**************/
		/**
		 * 调用打印机 不弹出窗口 适用于批量打印【三联单】
		 */
		String url10 = "http://localhost:4040/sf/waybill/print?type=V3.0.FM_poster_100mm210mm&output=noAlertPrint";
		/**
		 * 调用打印机 弹出窗口 可选择份数 适用于单张打印【三联单】
		 */
		String url11 = "http://localhost:4040/sf/waybill/print?type=V3.0.FM_poster_100mm210mm&output=print";

		/**
		 * 直接输出图片的BASE64编码字符串 可以使用html标签直接转换成图片【三联单】
		 */
		String url12 = "http://localhost:4040/sf/waybill/print?type=V3.0.FM_poster_100mm210mm&output=image";
       
		
		//根据业务需求确定请求地址
		String reqURL=url3;
		
		//电子面单顶部是否需要logo 
		boolean topLogo=true;//true 需要logo  false 不需要logo
		if(reqURL.contains("V2.0")&&topLogo){
			reqURL=reqURL.replace("V2.0", "V2.1");
		}
//		
//		if(reqURL.contains("V3.0")&&topLogo){
//			reqURL=reqURL.replace("V3.0", "V3.1");
//		}
	    
		System.out.println(reqURL);
		
        /**注意 需要使用对应业务场景的url  **/
		URL myURL = new URL(reqURL);
	     
		 //其中127.0.0.1:4040为打印服务部署的地址（端口如未指定，默认为4040），
		 //type为模板类型（支持两联、三联，尺寸为100mm*150mm和100mm*210mm，type为poster_100mm150mm和poster_100mm210mm）
		 //A5 poster_100mm150mm   A5 poster_100mm210mm
		 //output为输出类型,值为print或image，如不传，
		 //默认为print（print 表示直接打印，image表示获取图片的BASE64编码字符串）
		 //V2.0/V3.0模板顶部是带logo的  V2.1/V3.1顶部不带logo
		
		HttpURLConnection httpConn = (HttpURLConnection) myURL.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setUseCaches(false);
		httpConn.setRequestMethod("POST");
		//httpConn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
		httpConn.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
		
		
		httpConn.setConnectTimeout(5000);
		httpConn.setReadTimeout(2 * 5000);
		
		List<WaybillDto> waybillDtoList = new ArrayList<WaybillDto>();
		WaybillDto dto = new WaybillDto();
		
		
		
	    //这个必填 
		dto.setAppId("GDBXXKJ");//对应clientCode
		dto.setAppKey("95lAp6M4wNDXSnE829KpYvFuda3qNPnQ");//对应checkWord
        

	
        dto.setMailNo("755123456789");
        //dto.setMailNo("755123456788,001000000002");//子母单方式
		
		
		
		//收件人信息  
		dto.setConsignerProvince("广东省");
		dto.setConsignerCity("深圳市");
		dto.setConsignerCounty("南山区");
		dto.setConsignerAddress("学府路软件产业基地2B12楼5200708号"); //详细地址建议最多30个字  字段过长影响打印效果
//		dto.setConsignerCompany("神一样的科技");
		dto.setConsignerMobile("15893799999");
		dto.setConsignerName("风一样的旭哥");
//		dto.setConsignerShipperCode("518052");
//		dto.setConsignerTel("0755-33123456");
		
		
		//寄件人信息
		dto.setDeliverProvince("浙江省");
		dto.setDeliverCity("杭州市");
		dto.setDeliverCounty("拱墅区");
		dto.setDeliverCompany("江苏工电宝信息科技有限公司");
		dto.setDeliverAddress("舟山东路708号古墩路北（玉泉花园旁）百花苑西区7-2-201室");//详细地址建议最多30个字  字段过长影响打印效果
		dto.setDeliverName("张三");
		dto.setDeliverMobile("0571-26508888");
		dto.setDeliverShipperCode("310000");
//		dto.setDeliverTel("0571-26508888");
		
		
//		dto.setDestCode("755");//目的地代码 参考顺丰地区编号
//		dto.setZipCode("571");//原寄地代码 参考顺丰地区编号
		
		//签回单号  签单返回服务 会打印两份快单 其中第二份作为返寄的单
		//如客户使用签单返还业务则需打印“POD”字段，用以提醒收派员此件为签单返还快件。	
		// dto.setReturnTrackingNo("755123457778");
		
		//陆运E标示
		//业务类型为“电商特惠、顺丰特惠、电商专配、陆运件”则必须打印E标识，用以提示中转场分拣为陆运	
		dto.setElectric("E");
		
		
		//快递类型	
		//1 ：标准快递   2.顺丰特惠   3： 电商特惠   5：顺丰次晨  6：顺丰即日  7.电商速配   15：生鲜速配		
		dto.setExpressType(1);
				
		//COD代收货款金额,只需填金额, 单位元- 此项和月结卡号绑定的增值服务相关
//		dto.setCodValue("999.9");
		
//		dto.setInsureValue("501");//声明货物价值的保价金额,只需填金额,单位元
		dto.setMonthAccount("5125865576");//月结卡号  
		dto.setPayMethod(3);//
		
		
		/**丰密运单相关-如非使用丰密运单模板 不需要设置以下值**/
//		dto.setDestRouteLabel("755WE-571A3");
//		dto.setPrintIcon("1111");
//		dto.setProCode("T6");
//		dto.setAbFlag("A");
//		dto.setXbFlag("XB");
//		dto.setCodingMapping("F33");
//		dto.setCodingMappingOut("1A");
//		dto.setDestTeamCode("012345678");
//		dto.setSourceTransferCode("021WTF");
//		//对应下订单设置路由标签返回字段twoDimensionCode 该参数是丰密面单的二维码图
//		dto.setQRCode("MMM={'k1':'755WE','k2':'755BF','k3':'','k4':'T6','k5':'755123456789','k6':'A'}");
		//客户个性化Logo 必须是个可以访问的图片本地路径地址或者互联网图片地址
	   // dto.setCustLogo("D:\\qiao.jpg");
	    
		
		//加密项
		dto.setEncryptCustName(true);//加密寄件人及收件人名称
		dto.setEncryptMobile(true);//加密寄件人及收件人联系手机		
		
		
		
//		CargoInfoDto cargo = new CargoInfoDto();
//		cargo.setCargo("苹果7S");
//		cargo.setCargoCount(1);
//		cargo.setCargoUnit("件");
//		cargo.setSku("00015645");
//		cargo.setRemark("手机贵重物品 小心轻放");
//		
//		CargoInfoDto cargo2 = new CargoInfoDto();
//		cargo2.setCargo("苹果macbook pro");
//		cargo2.setCargoCount(1);
//		cargo2.setCargoUnit("件");
//		cargo2.setSku("00015646");
//		cargo2.setRemark("笔记本贵重物品 小心轻放");
//		
//		List<CargoInfoDto> cargoInfoList = new ArrayList<CargoInfoDto>();
//		cargoInfoList.add(cargo2);
//		cargoInfoList.add(cargo);
		
//		dto.setCargoInfoDtoList(cargoInfoList);
		waybillDtoList.add(dto);
		
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter stringWriter = new StringWriter();
		objectMapper.writeValue(stringWriter,waybillDtoList);
		
		httpConn.getOutputStream().write(stringWriter.toString().getBytes());
		
		System.out.println("请求参数： "+stringWriter.toString());
		
		httpConn.getOutputStream().flush();
		httpConn.getOutputStream().close();
		InputStream in = httpConn.getInputStream();
				
		BufferedReader in2=new BufferedReader(new InputStreamReader(in));

		String y="";
		
	    
		String strImg="";
		while((y=in2.readLine())!=null){
			 
				strImg=y.substring(y.indexOf("[")+1,y.length()-"]".length()-1);
				if(strImg.startsWith("\"")){
					strImg=strImg.substring(1,strImg.length());
				}
				if(strImg.endsWith("\"")){
					strImg=strImg.substring(0,strImg.length()-1);
				}
				  
			   }
	    
	        //将换行全部替换成空    
		strImg=strImg.replace("\\n", ""); 	    
//		System.out.println(strImg); 
		
	    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String dateStr = format.format(new Date());
		
		if(strImg.contains("\",\"")){
			//如子母单及签回单需要打印两份或者以上
			String[] arr = strImg.split("\",\""); 

			/**输出图片到本地 支持.jpg、.png格式**/		
		    for(int i = 0; i < arr.length; i++) {  
		    	Base64ImageTools.generateImage(arr[i].toString(), "D:\\qiaoWay"+dateStr+"-"+i+".jpg");
		    	
		    }
			}else{
	            Base64ImageTools.generateImage(strImg, "D:\\qiaoWaybill"+dateStr+".jpg");	
				
			}
	
	
    //     System.exit(0);
	}

	
}
