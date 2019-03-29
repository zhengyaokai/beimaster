package bmatser.model;

import bmatser.pageModel.PageMode.Channel;
import bmatser.util.LoginInfo;

import java.io.Serializable;

public class LoginInfoUtil
  implements Serializable
{
  private static final long serialVersionUID = 3550064441482462834L;
  private LoginInfo loginInfo;

  public String getUsername()
  {
    return this.loginInfo.getUsername();
  }

  public String getLoginId() {
    return this.loginInfo.getLoginId();
  }

  public String getDealerName() {
    return this.loginInfo.getDealerName();
  }

  public String getDealerId() {
    return this.loginInfo.getDealerId();
  }

  public String getCash() {
    return this.loginInfo.getCash();
  }

  public String getCheckStatus() {
    return this.loginInfo.getCheckStatus();
  }

  public String getIsLicense() {
    return this.loginInfo.getIsLicense();
  }

  public String getScore() {
    return this.loginInfo.getScore();
  }

  public String getCartNum() {
    return this.loginInfo.getCartNum();
  }

  public String getDealerType() {
    return this.loginInfo.getDealerType(); }

  public int getRank() {
    return this.loginInfo.getRank();
  }

  public int getBean() {
    return this.loginInfo.getBean();
  }

  public void write(LoginInfo login)
  {
    this.loginInfo = login;
  }

  public LoginInfo getLoginInfo()
  {
    return this.loginInfo;
  }
  
	public Channel getChannel() {
		  return this.loginInfo.getChannel();
	}

}