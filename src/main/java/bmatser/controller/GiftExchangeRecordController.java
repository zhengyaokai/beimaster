package bmatser.controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.model.CrmCustomer;
import bmatser.model.Dealer;
import bmatser.model.DealerBusinessLicense;
import bmatser.model.DealerScore;
import bmatser.model.GiftExchangeRecord;
import bmatser.model.MemberFavorite;
import bmatser.service.DealerBusinessLicenseI;
import bmatser.service.DealerI;
import bmatser.service.GiftExchangeRecordI;
import bmatser.util.Constants;
import bmatser.util.CookieTools;
import bmatser.util.FileTypeUtil;
import bmatser.util.FileUpload;
import bmatser.util.JSONUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
/**
 * 礼品兑换记录
 * @author felx
 */
@Controller
@RequestMapping("giftExchangeRecord")
@Api(value="giftExchangeRecord", description="礼品兑换记录")
public class GiftExchangeRecordController {

	@Autowired
	private GiftExchangeRecordI giftExchangeRecordI;
	@Autowired
	private MemcachedClient memcachedClient;
	/**
	 * 查询 礼品兑换记录关联信息
	 * @param Integer dealerId
	 * @return 
	 */
	//@RequestMapping(value="/select/{page}", method=RequestMethod.GET)
	@RequestMapping(value="/select/{page}", method=RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "查询 礼品兑换记录关联信息")
	String select(HttpServletRequest request,Map mp,@ApiParam(value="jsonp返回")@RequestParam String callback,@ApiParam(value="页数") @PathVariable int page){//@PathVariable int page
		int rows = 10;
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setError("请登录");
			return callback + "(" + JSONUtil.objectToJson(res).toString() + ")";
		}
		try {
			mp=new HashMap();
			mp.put("dealerId", Integer.parseInt(loginInfo.getDealerId()));
			mp.put("page",(page-1)*rows); 
			mp.put("rows",rows);
			List strList= giftExchangeRecordI.selectCreExchange(mp);
			int total = giftExchangeRecordI.count(mp);
			Map m=new HashMap();
			m.put("list",strList);
			m.put("total", total);	
			res.setData(m);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return callback + "(" + JSONUtil.objectToJson(res).toString() + ")";
	}
	
	/**
	 * APP查询 礼品兑换记录关联信息
	 * @param Integer dealerId
	 * @return 
	 */
	@RequestMapping(value="/selectAppGift/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "APP查询礼品兑换记录关联信息")
	public ResponseMsg  select(Map mp,@ApiParam(value="页数") @PathVariable int page,
			@ApiParam(value="dealerId")@RequestParam String dealerId){//@PathVariable int page
		int rows = 10;
		ResponseMsg res = new ResponseMsg();
		if(StringUtils.isBlank(dealerId)){
			res.setError("请登录");
			return res;
		}
		try {
			mp=new HashMap();
			mp.put("dealerId", Integer.parseInt(dealerId));
			mp.put("page",(page-1)*rows); 
			mp.put("rows",rows);
			List strList= giftExchangeRecordI.selectCreExchange(mp);
			int total = giftExchangeRecordI.count(mp);
			Map m=new HashMap();
			m.put("list",strList);
			m.put("total", total);	
			res.setData(m);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	
	/**
	 * APP查询 礼品兑换记录详情
	 * @return 
	 */
	@RequestMapping(value="/selectAppGiftInfo", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = " APP查询 礼品兑换记录详情")
	public ResponseMsg  selectAppGiftInfo(@ApiParam(value="dealerId")@RequestParam String dealerId,
			@ApiParam(value="礼品记录id")@RequestParam String id){
		ResponseMsg res = new ResponseMsg();
		if(StringUtils.isBlank(dealerId)){
			res.setError("请登录");
			return res;
		}	
		try {
			if(StringUtils.isBlank(id)){
				throw new RuntimeException("参数错误!");
			}
		    Map m = giftExchangeRecordI.selectAppGiftInfo(dealerId,id);
			res.setData(m);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	
	/**
	 * 积分兑换
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addGiftRecord", method=RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "积分兑换")
	public String addGiftRecord(HttpServletRequest request,@ApiParam(value="jsonp返回")@RequestParam String callback){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return callback + "(" + JSONUtil.objectToJson(res).toString() + ")";
		}
		Map mp=new HashMap();
		mp.put("dealerId",loginInfo.getDealerId());
		GiftExchangeRecord gr=new GiftExchangeRecord();
		try {
			Long m=giftExchangeRecordI.updateScoreById(request,mp,gr);
			if(m!=null){
				String cookieId = CookieTools.getCookieValue(Constants.saasLoginCookieName,request);
				cookieId = MemberTools.getFingerprint(request,cookieId);
				loginInfo.setScore(request.getSession().getAttribute("score").toString());
				memcachedClient.set(cookieId, 60 * 30,loginInfo);//30分钟
				res.setMsg("积分兑换成功");
				res.setData(loginInfo.getScore());
				res.setCode(0);
			}else{
				res.setMsg("积分兑换失败");
				res.setData(loginInfo.getScore());
				res.setCode(-1);
			}
		} catch (Exception e) {
			res.setError(e);
		}
		return callback + "(" + JSONUtil.objectToJson(res).toString() + ")";
	}
	
	/**
	 * APP积分兑换
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addAppGiftRecord", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "APP积分兑换")
	public ResponseMsg addAppGiftRecord(HttpServletRequest request,@ApiParam(value="dealerId")@RequestParam String dealerId){
		ResponseMsg res = new ResponseMsg();
		if(StringUtils.isBlank(dealerId)){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		Map mp=new HashMap();
		mp.put("dealerId",dealerId);
		GiftExchangeRecord gr=new GiftExchangeRecord();
		try {
			Long m=giftExchangeRecordI.updateScoreById(request,mp,gr);	
			if(m!=null){
				res.setMsg("积分兑换成功");
				mp.put("id", m);
				mp.put("score", request.getSession().getAttribute("score").toString());
				res.setData(mp);
				res.setCode(0);
			}else{
				res.setMsg("积分兑换失败");
				mp.put("id", m);
				mp.put("score", request.getSession().getAttribute("score").toString());
				res.setData(mp);
				res.setCode(-1);
			}
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
}