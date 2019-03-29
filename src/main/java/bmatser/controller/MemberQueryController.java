package bmatser.controller;

import java.math.BigDecimal;
import java.net.URLDecoder;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;


import bmatser.model.MemberQuery;
import bmatser.pageModel.BuyerConsignAddressPage;
import bmatser.service.AdverImageI;
import bmatser.service.MemberQueryI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
/**
 * 会员咨询
 * @author felx
 */
@Controller
@RequestMapping("memberQuery")
@Api(value="memberQuery", description="会员咨询")
public class MemberQueryController {

	@Autowired
	private MemberQueryI memberQueryI;
	/**
	 * 查询会员咨询关联信息
	 * @param HttpServletRequest request,Map mp
	 * @return 
	 */
	@RequestMapping(value="/select/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询会员咨询关联信息")
	ResponseMsg select(HttpServletRequest request,Map mp,@ApiParam(value="页数") @PathVariable int page){
		int rows = 10;
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			mp=new HashMap();
			mp.put("page",(page-1)*rows); 
			mp.put("rows",rows);
			mp.put("dealerId",loginInfo.getDealerId());
			List list= memberQueryI.selectById(request,mp);
			Long total = memberQueryI.count(mp);
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
	 * 新增会员咨询
	 * @param HttpServletRequest request,MemberFavorite memberFavorite
	 * @return 
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "新增会员咨询")
	public ResponseMsg add(HttpServletRequest request,@ModelAttribute MemberQuery memberQuery){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			memberQuery.setDealerId(Integer.parseInt(loginInfo.getDealerId()));
			memberQuery.setQueryContent(memberQuery.getQueryContent());
			int i = memberQueryI.insert(memberQuery);
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
}
