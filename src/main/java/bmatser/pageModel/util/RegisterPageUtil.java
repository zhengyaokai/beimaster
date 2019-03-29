package bmatser.pageModel.util;

import org.apache.commons.lang3.StringUtils;

import bmatser.pageModel.RegisterPage;
import bmatser.util.DESCoder;
import bmatser.util.Encrypt;

public class RegisterPageUtil extends RegisterPage{
	
	
	private static final long serialVersionUID = -4297480041289374929L;
	
	public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	/**
	 * 验证(商城)
	 * @return
	 * @throws Exception
	 */
	public boolean checkMallInfo() throws Exception{
		if(StringUtils.isNotBlank(this.dealerType)){
			if(StringUtils.isBlank(this.userName)){
				throw new Exception("用户名错误");
			}
			if(StringUtils.isBlank(this.password)){
				throw new Exception("用户名错误");
			}
			if(StringUtils.isBlank(this.mobile)){
				throw new Exception("手机错误");
			}
			switch (this.dealerType) {
				case "4":
				break;
				case "5":
					if(StringUtils.isBlank(this.dealerName)){
						throw new Exception("公司名称错误");
					}
					if(StringUtils.isBlank(this.operateScope)){
						throw new Exception("客户行业错误");
					}
					if(StringUtils.isBlank(this.saleCustomerGroup)){
						throw new Exception("客户性质错误");
					}
				break;
			default:
				throw new Exception("用户类型错误");
			}
		}else{
			throw new Exception("用户类型错误");
		}
		return true;
	}
	/**
	 * 保存注册默认信息(商城)
	 * @throws Exception
	 */
	public void setDefaultMallInfo() throws Exception{
		DESCoder.instanceMobile();
		String mobile = this.mobile;
		String password = this.password;
		this.setMobileSecret(DESCoder.encoder(mobile).trim());
		this.setMobile(DESCoder.getNewTel(mobile));
		if(StringUtils.isNotBlank(this.linkTel)){
			String linkTel = this.linkTel;
			this.setLinkTelSecret(DESCoder.encoder(linkTel).trim());
			this.setLinkTel(DESCoder.getNewTel(linkTel));
		}
		if(StringUtils.isBlank(this.dealerName)){
			this.setDealerName(this.userName);
		}
		this.setPassword(Encrypt.md5AndSha(password));
		this.setCheckStatus("1");
		this.setUserNameFlag("M");
		this.setCash(10000);
	}
	
}
