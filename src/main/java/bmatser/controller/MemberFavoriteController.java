package bmatser.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;


import bmatser.model.MemberFavorite;
import bmatser.pageModel.BuyerConsignAddressPage;
import bmatser.service.AdverImageI;
import bmatser.service.MemberFavoriteI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
/**
 * @author felx
 */
@Controller
@RequestMapping("memberFavorite")
@Api(value="memberFavorite", description="会员")
public class MemberFavoriteController {

	@Autowired
	private MemberFavoriteI memberFavoriteI;
	
	/**
	 * 查询会员收藏关联信息
	 * @param HttpServletRequest request,Map mp
	 * @return 
	 */
	@RequestMapping(value="/select/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询会员收藏关联信息")
	ResponseMsg select(HttpServletRequest request,Map mp,
			@ApiParam(value="收藏开始日期") @RequestParam(required=false) String startTime,
			@ApiParam(value="收藏结束日期") @RequestParam(required=false) String endTime,
			@ApiParam(value="1:三天内；2：一个月内；3：三个月内") @RequestParam(required=false) String queryType,
			@ApiParam(value="页数") @PathVariable int page){
		int rows = 20;
		ResponseMsg res = new ResponseMsg();
		if(page<1){
			return res.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		/*String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String queryType = request.getParameter("queryType");*///1:三天内；2：一个月内；3：三个月内
		try {
			mp=new HashMap();
			mp.put("dealerId", Integer.parseInt(loginInfo.getDealerId()));
			mp.put("page",page*rows); 
			mp.put("rows",rows);
			mp.put("startTime", StringUtils.isNotBlank(startTime)?startTime : null);
			mp.put("endTime", StringUtils.isNotBlank(endTime)?endTime : null);
			mp.put("queryType", StringUtils.isNotBlank(queryType)?queryType : null);
			List list= memberFavoriteI.selectById(request,mp);
			int total = memberFavoriteI.count(mp);
			Map m=new HashMap();
			m.put("list",list);
			m.put("total", total);	
			res.setData(m);
			res.setCode(0);
		} catch (Exception e) {
			res.setMsg(e.toString());
			res.setCode(-1);
		}
		return res;
	}
	
	/**
	 * APP商城查询会员收藏关联信息
	 * @param HttpServletRequest request,Map mp
	 * @return 
	 */
	@RequestMapping(value="/selectMemberFavorite/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "APP商城查询会员收藏关联信息")
	ResponseMsg selectMemberFavorite(HttpServletRequest request,Map mp,
			@ApiParam(value="收藏开始日期") @RequestParam(required=false) String startTime,
			@ApiParam(value="收藏结束日期") @RequestParam(required=false) String endTime,
			@ApiParam(value="1:三天内；2：一个月内；3：三个月内") @RequestParam(required=false) String queryType,
			@ApiParam(value="页数") @PathVariable int page){
		int rows = 20;
		ResponseMsg res = new ResponseMsg();
		if(page<1){
			return res.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		String dealerId = request.getParameter("dealerId");
		if(StringUtils.isBlank(dealerId)){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		/*String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String queryType = request.getParameter("queryType");*///1:三天内；2：一个月内；3：三个月内
		try {
			mp=new HashMap();
			mp.put("dealerId", Integer.parseInt(dealerId));
			mp.put("page",page*rows); 
			mp.put("rows",rows);
			mp.put("startTime", StringUtils.isNotBlank(startTime)?startTime : null);
			mp.put("endTime", StringUtils.isNotBlank(endTime)?endTime : null);
			mp.put("queryType", StringUtils.isNotBlank(queryType)?queryType : null);
			List list= memberFavoriteI.selectById(request,mp);
			int total = memberFavoriteI.count(mp);
			Map m=new HashMap();
			m.put("list",list);
			m.put("total", total);	
			res.setData(m);
			res.setCode(0);
		} catch (Exception e) {
			res.setMsg(e.toString());
			res.setCode(-1);
		}
		return res;
	}
	
	/**
	 * 查询商品是否被收藏
	 * @param HttpServletRequest request
	 * @return 
	 */
	@RequestMapping(value="/getNum", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询商品是否被收藏")
	ResponseMsg getNum(HttpServletRequest request,@ApiParam(value="sellerGoodsId") @RequestParam String sellerGoodsId){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			int getNumber=0;
			if(StringUtils.isNotBlank(sellerGoodsId)){
				getNumber=memberFavoriteI.getNumber(Long.parseLong(request.getParameter("sellerGoodsId")), loginInfo.getDealerId());
			}
			res.setData(getNumber);
			res.setCode(0);
		} catch (Exception e) {
			res.setMsg(e.toString());
			res.setCode(-1);
		}
		return res;
	}
	
	/**
	 * App商城查询商品是否被收藏
	 * @param HttpServletRequest request
	 * @return 
	 */
	@RequestMapping(value="/getNum1", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "App商城查询商品是否被收藏")
	ResponseMsg getNum1(HttpServletRequest request,@ApiParam(value="dealerId") @RequestParam String dealerId,
			@ApiParam(value="sellerGoodsId") @RequestParam String sellerGoodsId){
		ResponseMsg res = new ResponseMsg();
		/*String dealerId = request.getParameter("dealerId");*/
		if(StringUtils.isBlank(dealerId)){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			int getNumber=0;
			if(StringUtils.isNotBlank(sellerGoodsId)){
				getNumber=memberFavoriteI.getNumber(Long.parseLong(request.getParameter("sellerGoodsId")), dealerId);
			}
			res.setData(getNumber);
			res.setCode(0);
		} catch (Exception e) {
			res.setMsg(e.toString());
			res.setCode(-1);
		}
		return res;
	}
	
	/**
	 * 新增会员收藏
	 * @param HttpServletRequest request,MemberFavorite memberFavorite
	 * @return 
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "新增会员收藏")
	public ResponseMsg add(HttpServletRequest request,@ModelAttribute MemberFavorite memberFavorite){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			int i = memberFavoriteI.insert(loginInfo,memberFavorite);
			if(i>0){
				res.setCode(0);
			}else{
				res.setCode(-1);
				res.setMsg("添加失败");
			}
		} catch (Exception e) {
			res.setCode(-1);
			res.setMsg("添加失败:"+e.toString());
		}
		return res;
	}
	
	/**
	 * APP商城新增会员收藏
	 * @param HttpServletRequest request,MemberFavorite memberFavorite
	 * @return 
	 */
	@RequestMapping(value="/addAppMemberFavorite",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "App商城新增会员收藏")
	public ResponseMsg addAppMemberFavorite(HttpServletRequest request,@ModelAttribute MemberFavorite memberFavorite){
		ResponseMsg res = new ResponseMsg();
		if(null == memberFavorite || null == memberFavorite.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			int i = memberFavoriteI.insertMemberFavorite(memberFavorite);
			if(i>0){
				res.setCode(0);
			}else{
				res.setCode(-1);
				res.setMsg("添加失败");
			}
		} catch (Exception e) {
			res.setCode(-1);
			res.setMsg("添加失败:"+e.toString());
		}
		return res;
	}
	
	/**
	 * 修改会员收藏
	 * @param HttpServletRequest request,String id,MemberFavorite memberFavorite
	 * @return 
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改会员收藏")
	public ResponseMsg edit(HttpServletRequest request,@ApiParam(value="会员收藏Id") @RequestParam String id,@ModelAttribute MemberFavorite memberFavorite) {
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			int i = memberFavoriteI.updateById(id,loginInfo,memberFavorite);
			if(i>0){
				res.setCode(0);
			}else{
				res.setCode(-1);
				res.setMsg("修改失败");
			}
		} catch (Exception e) {
			res.setCode(-1);
			res.setMsg("修改失败:" + e.toString());
		}
		return res;
	}
	/**
	 * 删除会员收藏
	 * @param HttpServletRequest request,Integer id
	 * @return 
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除会员收藏")
	public ResponseMsg delete(HttpServletRequest request,@ApiParam(value="sellerGoodsIds") @RequestParam String sellerGoodsIds) {
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		int i=0;
		if(StringUtils.isNotBlank(sellerGoodsIds)){
			i = memberFavoriteI.deleteById(sellerGoodsIds,Integer.parseInt(loginInfo.getDealerId()));
		}
		try {
			if(i>0){
				res.setCode(0);
			}else{
				res.setCode(-1);
				res.setMsg("删除失败");
			}
		} catch (Exception e) {
			res.setCode(-1);
			res.setMsg("删除失败:" + e.toString());
		}
		return res;
	}
	
	/**
	 * APP商城删除会员收藏
	 * @param HttpServletRequest request,Integer id
	 * @return 
	 */
	@RequestMapping(value="/deleteMemberFavorite",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "APP商城删除会员收藏")
	public ResponseMsg deleteMemberFavorite(HttpServletRequest request,@ApiParam(value="sellerGoodsIds") @RequestParam String sellerGoodsIds,
			@ApiParam(value="dealerId") @RequestParam String dealerId) {
		ResponseMsg res = new ResponseMsg();
		/*String dealerId = request.getParameter("dealerId");*/
		if(StringUtils.isBlank(dealerId)){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		int i=0;
		if(StringUtils.isNotBlank(sellerGoodsIds)){
			i = memberFavoriteI.deleteById(sellerGoodsIds,Integer.parseInt(dealerId));
		}
		try {
			if(i>0){
				res.setCode(0);
			}else{
				res.setCode(-1);
				res.setMsg("删除失败");
			}
		} catch (Exception e) {
			res.setCode(-1);
			res.setMsg("删除失败:" + e.toString());
		}
		return res;
	}
}
