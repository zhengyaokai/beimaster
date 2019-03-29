package bmatser.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.model.SellerGoods;
import bmatser.pageModel.PageMode;
import bmatser.service.GroupActivityI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("/group")
@Api(value="group", description="团购报名")
public class GroupActivityController {

	@Autowired
	private GroupActivityI groupActivityI;
	
	
	/**
	 * 团购报名接口开发
	 * @param request
	 * @param channel  saas pc端 
	 * @return
	 */
	@RequestMapping(value="/{channel}/add",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = " 团购报名接口")
	public ResponseMsg addGroupApply(HttpServletRequest request,@ApiParam(value="saas pc端 ") @PathVariable("channel")String channel,
			@ApiParam(value="团购活动ID") @RequestParam String groupId){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		/*LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}*/
		/*String groupId = request.getParameter("groupId");*/
		try {
			Map m = groupActivityI.addGroupApply(groupId,pageMode.getLogin().getDealerId());
			msg.setData(m);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 团购活动点击数更新接口开发
	 * @param request
	 * @param channel saas pc端 
	 * @return
	 */
	@RequestMapping(value="/{channel}/click",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = " 团购活动点击数更新接口")
	public ResponseMsg addClick(HttpServletRequest request,@ApiParam(value="saas pc端 ") @PathVariable("channel")String channel,
			@ApiParam(value="团购活动ID") @RequestParam String groupId){
		ResponseMsg msg = new ResponseMsg();
//		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		/*String groupId = request.getParameter("groupId");*/
//		if(null == loginInfo || null == loginInfo.getDealerId()){
//			return msg.setError("请先登录");
//		}
		try {
				groupActivityI.addClick(groupId);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
		
	}
}
