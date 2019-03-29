package bmatser.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="手机app")
public class ApplicationApp {
	
	 private Integer id;
	 @ApiModelProperty(value="'ios','android'") 
	 private String type;
	 @ApiModelProperty(value="平台类型") 
	 private Integer platform;
	 @ApiModelProperty(value="是否升级 0 不升级 1 提示升级 2 强势升级") 
	 private String is_update;
	 @ApiModelProperty(value="版本号") 
	 private String app_version;
	 @ApiModelProperty(value="加密key") 
	 private String app_key;
	 @ApiModelProperty(value="加密秘钥") 
	 private String app_secrect;
	 @ApiModelProperty(value="app下载地址,完整的网址") 
	 private String download;
	 
	 private Integer build;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public String getIs_update() {
		return is_update;
	}

	public void setIs_update(String is_update) {
		this.is_update = is_update;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getApp_secrect() {
		return app_secrect;
	}

	public void setApp_secrect(String app_secrect) {
		this.app_secrect = app_secrect;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public Integer getBuild() {
		return build;
	}

	public void setBuild(Integer build) {
		this.build = build;
	}
	 
	 
	 
}
