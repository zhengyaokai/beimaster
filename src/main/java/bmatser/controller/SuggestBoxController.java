package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import bmatser.pageModel.FeedBack;
import bmatser.service.SuggestBoxServiceI;
import bmatser.util.IpUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping(value="/suggest")
@Api(value="suggest", description="建议")
public class SuggestBoxController {
	
	@Autowired
	private SuggestBoxServiceI SuggestBoxI;
	/**
	 * 意见建议
	 * @param request
	 * @param feedBack
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "意见建议", 
	notes = "意见建议")
	public ResponseMsg addSuggest(HttpServletRequest request,FeedBack feedBack){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			if(StringUtils.isNotBlank(IpUtil.getIpAddr(request))){
				feedBack.setLoginIp(IpUtil.getIpAddr(request));
			}
			SuggestBoxI.addSuggest(feedBack,loginInfo);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
		
	}

}
