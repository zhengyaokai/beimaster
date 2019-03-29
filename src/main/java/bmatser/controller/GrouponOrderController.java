package bmatser.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import bmatser.service.GrouponOrderI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
/**
 * 
 * 团购商品 
 *
 */
@Controller
@RequestMapping("jhs")
@Api(value="jhs", description="团购商品")
public class GrouponOrderController {

	@Autowired
	private GrouponOrderI grouponOrderI;
	@Autowired
	private GroupActivityI groupActivityI;
	/**
	 * 
	 * 团购商品列表 
	 *
	 */
	@RequestMapping(value={"/order_goods/{page}","/app/order_goods/{page}"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "团购商品列表")
	public ResponseMsg selectCategory(HttpServletRequest request,@ApiParam(value="页数") @PathVariable int page){
		ResponseMsg res = new ResponseMsg();
		int rows;
		if(StringUtils.isBlank(request.getParameter("rows"))){
			rows = 10;
		}else{
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		PageMode pageMode = new PageMode(request);
		Map mp=new HashMap();
		Integer dealerId;
		
		/*LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return res.setError("请先登录");
		}*/
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;

			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
		}
			List grouponOrderAll=grouponOrderI.selectGrouponGoods(dealerId,request,(page-1)*rows,rows,mp);
			int total = grouponOrderI.count(mp);
			Map mp2=new HashMap();
			mp2.put("grouponOrderAll", grouponOrderAll);
			mp2.put("total", total);
			res.setData(mp2);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	
	/**
	 * 
	 * 活动汇 团购活动列表 
	 *
	 */
	@RequestMapping(value={"/list/{page}","/app/list/{page}"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "活动汇 团购活动列表")
	public ResponseMsg selectGrouponActivitySink(HttpServletRequest request,@ApiParam(value="页数") @PathVariable int page){
		ResponseMsg res = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		int rows;
		if(StringUtils.isBlank(request.getParameter("rows"))){
			rows = 10;
		}else{
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map mp=new HashMap();
		Integer dealerId;
		/*LoginInfo loginInfo = MemberTools.isSaasLogin(request);*/
//		if(null == loginInfo || null == loginInfo.getDealerId()){
//			return res.setError("请先登录");
//		}
		try {
			switch (pageMode.channel()) {
			case "app":
				if(pageMode.isLogin("app")){
					dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
					mp.put("dealerId", dealerId);
				}
             	break;

			default:
				if(pageMode.isLogin("saas")){
					dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
					mp.put("dealerId", dealerId);
				}				
				break;
			}
			/*if(null != loginInfo){
				mp.put("dealerId", loginInfo.getDealerId());
			}*/
			List activitySinkAll=grouponOrderI.selectGrouponActivitySink(request,(page-1)*rows,rows,mp);
			int total = grouponOrderI.countActivitySink(mp);
			Map mp2=new HashMap();
			mp2.put("activitySinkAll", activitySinkAll);
			mp2.put("total", total);
			res.setData(mp2);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	
	/**
	 * 
	 * 活动汇 单个团购活动详情  
	 *
	 */
	@RequestMapping(value={"/detail","/app/detail"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "单个团购活动详情")
	public ResponseMsg selectGrouponActivityDetail(HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
		/*LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return res.setError("请先登录");
		}*/
		PageMode pageMode = new PageMode(request);
		String dealerId;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = pageMode.getAppLogin().getDealerId();
				groupActivityI.addClick(pageMode.getStringValue("id"));//增加点击量
				break;

			default:
				dealerId = pageMode.getSaasLogin().getDealerId();
				break;
		}
			res.setData(grouponOrderI.selectGrouponActivityDetail(request,dealerId));
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	
	/**
	 * 
	 * 团购详情列表矩阵  
	 *
	 */
	@RequestMapping(value={"/goods_list/{page}","/app/goods_list/{page}"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "团购详情列表矩阵")
	public ResponseMsg selectGrouponActivityDetailList(HttpServletRequest request,@ApiParam(value="页数") @PathVariable int page,
			@ApiParam(value="团购活动ID") @RequestParam String id){
		ResponseMsg res = new ResponseMsg();
		int rows = 15;
		/*LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return res.setError("请先登录");
		}*/
		PageMode pageMode = new PageMode(request);
		String dealerId;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = pageMode.getAppLogin().getDealerId();
				break;

			default:
				dealerId = pageMode.getSaasLogin().getDealerId();
				break;
		}
			/*String id=request.getParameter("id");*/
			Map mp=new HashMap();
			mp.put("id",id);
			List activityDetailList=grouponOrderI.selectGrouponActivityDetailList(id,dealerId,(page-1)*rows,rows);
			int total = grouponOrderI.countActivityDetailList(mp);
			Map mp2=new HashMap();
			mp2.put("activitySinkAll", activityDetailList);
			mp2.put("total", total);
			res.setData(mp2);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
}
