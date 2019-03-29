package bmatser.pageModel;

import java.io.Serializable;

/**
 * @description 商城搜索日志
 * @author felx
 * @version 1.0
 * @date 2017-03-31 上午
 */
public class MallKeywordLogPage implements Serializable {


	private static final long serialVersionUID = -5209910550232254426L;
	/**
	 * 
	 */
	
	/* 日志Id */
	private String logId;
	/* 登录人Id */
	private String loginId;
	/* 用户名 */
	private String loginName;
	/* 搜索时间 */
	private String searchTime;
	/* 关键词 */
	private String keyword;
	/* ip地址 */
	private String ip;	
	/* 搜索结果数量 */
	private String resultCount;
	/* 搜索类型 1：关键字，2：分类：3：品牌*/
	private String searchType;
	
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getResultCount() {
		return resultCount;
	}
	public void setResultCount(String resultCount) {
		this.resultCount = resultCount;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return logId+"---"+ loginId+"---"+ loginName+"---"+
				searchTime+"---"+ keyword+"---"+ ip+"---"+ resultCount;
	}


}
