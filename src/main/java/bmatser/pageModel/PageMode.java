package bmatser.pageModel;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

import bmatser.util.Encrypt;
import bmatser.util.IpUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
/**
 * 获取页面参数工具类
 * @author felx
 *
 */
public class PageMode {
	private final String[] webType = {"web_item","app","mall","saas"}; 
	private Map<String, Object> page;
	private List<String> keyArray;
	private LoginInfo saasLogin;
	private LoginInfo mallLogin;
	private LoginInfo appLogin;
	private String channel="";
	private String ip;
	
	public enum Channel{
		SAAS,APP,MALL
	}
	
	/**
	 * 获取页面传递的参数
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public PageMode(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		if(this.page==null){
			this.page = new HashMap<String, Object>();
			this.keyArray = new ArrayList<String>();
		}
		Map<String, String[]> map = request.getParameterMap();
		for (Map.Entry<String, String[]> entity : map.entrySet()) {
			this.page.put(entity.getKey(), entity.getValue()[0]);
			this.keyArray.add(entity.getKey());
		}

		this.setIp(IpUtil.getIpAddr(request));
		saasLogin = MemberTools.isSaasLogin(request);
		mallLogin = MemberTools.isLogin(request);
		if(this.contains("dealerId") || this.contains("loginId")){
			appLogin = new LoginInfo();
			appLogin.setDealerId(getStringValue("dealerId"));
			appLogin.setLoginId(getStringValue("loginId"));
		}

		initLoginInfo(request);

		for (String s : webType) {
			if(url.indexOf("/"+s+"/")!=-1){
				this.channel = s;
				break;
			};
		}
	}
	/**
	 * 初始化登录信息
	 * @param request
	 */
	private void initLoginInfo(HttpServletRequest request) {
		this.saasLogin = MemberTools.isSaasLogin(request);
		this.mallLogin = MemberTools.isLogin(request);
		if(this.contains("dealerId") || this.contains("loginId") || this.contains("buyerId")){
			String dealerId= getStringValue("dealerId");
			String loginId= getStringValue("loginId");
			String buyerId= getStringValue("buyerId");
			this.appLogin = new LoginInfo();
			this.appLogin.setDealerId(dealerId);
			if(StringUtils.isBlank(dealerId)){
				this.appLogin.setDealerId(buyerId);
			}
			this.appLogin.setLoginId(loginId);
		}
	}
	
	/**
	 * 验证参数是否存在
	 * @param s
	 * @return
	 */
	public boolean contains(String ... value ) {
		if(value.length>0){
			for (String s : value) {
				if(this.page!=null && this.page.containsKey(s)){
					Object  param= this.page.get(s);
					if(param==null || "".equals(String.valueOf(param))){
						return false;
					}
				}else{
					return false;
				}
			}
		}
		return true;
	}
	/**
	 *	获得String类型的value值
	 * @param key
	 * @return
	 */
	public String getStringValue(String key){
		return this.page.get(key)!=null?String.valueOf(this.page.get(key)) : "";
	}
	/**
	 *	获得Object类型的value值
	 * @param key
	 * @return
	 */
	public Object getValue(String key){
		return this.page.get(key);
	}
	
	/**
	 *	获得BigDecimal类型的value值
	 * @param key
	 * @return
	 */
	public BigDecimal getBigDecimalValue(String key){
		return new BigDecimal(String.valueOf(page.get(key)));
	}
	/**
	 * 存入参数
	 * @param key
	 * @param value
	 * @return
	 */
	public PageMode  put(String key,Object value){
		this.page.put(key, value);
		return this;
	}
	/**
	 * 获取key的集合
	 * @return
	 */
	public List<String> getKeyArray() {
		return keyArray;
	}
	/**
	 * 分割value值
	 * @param param
	 * @param regex
	 * @return
	 */
	public String[] split(String param,String regex){
		String s = String.valueOf(this.page.get(param));
		if(s.indexOf(",", s.length()-1)>0){
			s=s+",";
		}
		s = s.replaceAll(",,", ", ,");
		return s.split(regex);
	}
	/**
	 * 参数转码
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String decode(String param) throws Exception{
		if(this.page.get(param)==null){
			return null;
		}else{
			return URLDecoder.decode(getStringValue(param), "UTF-8");
		}
	}
	/**
	 * 获取Saas登陆
	 * @return
	 * @throws Exception 
	 */
	public LoginInfo getSaasLogin() throws Exception {
		if(saasLogin==null){
			throw new Exception("请登录网站");
		}
		return saasLogin;
	}

	/**
	 * 获取商城登陆
	 * @return
	 * @throws Exception 
	 */
	public LoginInfo getMallLogin() throws Exception {
		if(mallLogin==null){
			throw new Exception("请登录商城");
		}
		return mallLogin;
	}

	/**
	 * 获取App登陆
	 * @return
	 * @throws Exception 
	 */
	public LoginInfo getAppLogin() throws Exception {
		if(appLogin==null){
			throw new Exception("请登录App");
		}
		return appLogin;
	}
	/**
	 * 判断是否登陆
	 * @param param
	 * @return
	 */
	@Deprecated
	public boolean isLogin(String param){
		boolean b = false;
		if(param != null){
			param = param.toLowerCase();
		switch (param) {
			case "saas":
				b = saasLogin !=null;
				break;
			case "app":
				b = appLogin !=null;
				break;
			case "mall":
				b = mallLogin != null;
				break;
		}
		}
		return b;
	}
	
	/**
	 * 判断是否登陆(枚举 安全)
	 * @param param
	 * @return
	 */
	public boolean isLogin(Channel param){
		Boolean b = null;
		switch (param) {
		case SAAS:
			b = saasLogin !=null;
			break;
		case APP:
			b = appLogin !=null;
			break;
		case MALL:
			b = mallLogin != null;
			break;
		}
		return b;
	}
	
	/**
	 * 获取加密数据
	 * @param param
	 * @return
	 */
	public String getMd5AndSha(String param){
		String value = this.getStringValue(param);
		if(StringUtils.isNotBlank(value)){
			return Encrypt.md5AndSha(value);
		}
		return "";
	}
	/**
	 * 获取页面渠道(默认现在只有三种SAAS,APP,MALL)
	 * @return
	 */
	public String channel() {
		return channel;
	}
	
	/**
	 * 自动判断是否登陆
	 * @param param
	 * @return
	 */
	public boolean isAutoLogin(){
		if(isLogin(this.channel)){
			return true;
		}
		return false;
	}
	
	/**
	 * 自动获得登录信息
	 * @return
	 * @throws Exception
	 */
	public LoginInfo getLogin() throws Exception {
			switch (this.channel) {
			case "saas":
				return this.getSaasLogin();
			case "app":
				return this.getAppLogin();
			case "mall":
				return this.getMallLogin();
			case "web_item":
				return this.getSaasLogin();
			default:
				throw new Exception("登录信息不存在");
		}
	}

	/**
	 * 获取零售商Id(字符串类型)
	 * @param param 获取Id的渠道
	 * @return
	 * @throws Exception
	 */
	public String getDealerId(Channel param) throws Exception{
		switch (param) {
		case SAAS:
			return this.getSaasLogin().getDealerId();
		case APP:
			return this.getAppLogin().getDealerId();
		case MALL:
			return this.getMallLogin().getDealerId();
		default:
			throw new Exception("渠道不存在");
		}
	}
	/**
	 * 获取零售商Id(BigDecimal类型)
	 * @param param 获取Id的渠道
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getDealerIdDecimalVal(Channel param) throws Exception{
		return new BigDecimal(getDealerId(param));
	}
	
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public long getIp2Long() {
		String[] ipAddressInArray;
		long result = 0;
		if(StringUtils.isBlank(this.ip)){
			return 0;
		}else if("本地".equals(this.ip)){
			ipAddressInArray = "127.0.0.1".split("\\.");
		}else{
			ipAddressInArray = this.ip.split("\\.");
		}
		for (int i = 3; i >= 0; i--) {  
		    long ip = Long.parseLong(ipAddressInArray[3 - i]);  
		    result |= ip << (i * 8);  
		}  
		return result; 
	}
	public Map<String, Object> getData() {
		return page;
	}
	/**
	 * 页面参数转javaBean
	 * @param clazz
	 * @return
	 */
	public <T> T toJavaObject(Class<T> clazz){
		JSONObject jo = new JSONObject();
		for (Entry<String, Object> s : this.page.entrySet()) {
			jo.put(s.getKey(), s.getValue());
		}
		return JSONObject.toJavaObject(jo, clazz);
	}
	
	public int getIntValue(String param) {
		Object o= this.page.get(param);
		if(o == null){
			throw new NullPointerException();
		}
		if(o instanceof Integer){
			return (int)o;
		}
		if(o instanceof Number){
			return ((Number)o).intValue();
		}
		if(o instanceof BigDecimal){
			return ((BigDecimal)o).intValue();
		}else{
			return new BigDecimal(o.toString()).intValue();
		}
	}

	

	
}

