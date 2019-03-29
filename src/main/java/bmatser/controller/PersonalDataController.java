package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.annotations.Login;
import bmatser.annotations.ResponseDefault;
import bmatser.model.LoginInfoUtil;
import bmatser.pageModel.MyInfoPage;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.PersonalDataI;
import bmatser.util.Constants;
import bmatser.util.FileTypeUtil;
import bmatser.util.FileUpload;
import bmatser.util.JSONUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
/**
 * 
 * @author felx
 *
 */
@Controller
@RequestMapping("customer_info")
@Api(value="customer_info", description="个人资料")
public class PersonalDataController {

	@Autowired
	private PersonalDataI personalDataI;
	
	/**
	 * Saas查看个人资料(移动端新版)
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/{dealerId}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Saas查看个人资料(移动端新版)", 
	notes = "Saas查看个人资料(移动端新版)")
	public ResponseMsg  findRegister(HttpServletRequest  request,@PathVariable("dealerId")String dealerId){
		return getPersionData(request,dealerId);
	}
	/**
	 * Saas查看个人资料(新版)
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/home",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Saas查看个人资料(新版)", 
	notes = "Saas查看个人资料(新版)")
	public ResponseMsg  findSaasRegister(HttpServletRequest  request){
		return getPersionData(request,null);
	}
	
	/**
	 * 积分商城查看个人资料
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/jfsc",method=RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "Saas查看个人资料(新版)", 
	notes = "Saas查看个人资料(新版)")
	public String  findjfscRegister(HttpServletRequest  request,@ApiParam(value="jsonp返回")@RequestParam String callback){
		ResponseMsg msg = new ResponseMsg();
		msg = getPersionData(request,null);
		return callback + "(" + JSONUtil.objectToJson(msg).toString() + ")";
	}
	
	/**
	 * 查看个人资料
	 * @param req
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看个人资料", 
	notes = "查看个人资料")
	public ResponseMsg  findRegister(HttpServletRequest  req){
		ResponseMsg msg = new ResponseMsg();
		try {
			LoginInfo login = MemberTools.isLogin(req);
			if(login!=null){
				msg.setData(personalDataI.findRegister(login.getDealerId()));
			}else{
				throw new Exception("请登录");
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setData("查询错误" + e.getMessage());
		}
		return msg;
	}


	/**
	 * 更新个人资料
	 * @param req
	 * @param info
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "更新个人资料", 
	notes = "更新个人资料")
	public ResponseMsg  updateRegister(HttpServletRequest  req,MyInfoPage info){
		ResponseMsg msg = new ResponseMsg();
		try {
			LoginInfo login = MemberTools.isLogin(req);
			if(login!=null){
				info.setId(login.getDealerId());
				personalDataI.updateRegister(info);
			}else{
				throw new Exception("请登录");
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("更新失败:" + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 商城app更新个人资料
	 * @param req
	 * @param info
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城app更新个人资料", 
	notes = "商城app更新个人资料")
	public ResponseMsg  updateAppMallRegister(HttpServletRequest  req,MyInfoPage info,String dealerId){
		ResponseMsg msg = new ResponseMsg();
		try {
			if(StringUtils.isNotBlank(dealerId)){
				info.setId(dealerId);
				personalDataI.updateRegister(info);
			}else{
				throw new Exception("请登录");
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("更新失败:" + e.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * 头像上传
	 * @param file 上传文件
	 * @return
	 * @throws JFigException
	 */
    @RequestMapping(value="/upload",method=RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "头像上传", 
	notes = "头像上传")
    public ResponseMsg upload(@RequestParam(value="uploadFile")MultipartFile file) throws JFigException{  
    	ResponseMsg reponseMsg = new ResponseMsg();
    	String filePath = JFig.getInstance().getValue("UPLOAD_PATH", "PERSION_IMAGE");
    	String path = null;
    	try {
	    	if(FileTypeUtil.isImage(file.getInputStream())){
				path = FileUpload.getUpload(file,new StringBuffer(filePath));
				path = Constants.BASE_URL_NET + filePath.substring(filePath.lastIndexOf("/")+1) + path;;
				reponseMsg.setCode(0);
				reponseMsg.setMsg(path);
			}else{
				reponseMsg.setCode(-1);
				reponseMsg.setMsg("图片不合法");
			}
		} catch (Exception e) {
			reponseMsg.setCode(0);
			reponseMsg.setMsg("图片失败:"+e.getMessage());
		}
	    return reponseMsg;  
	}
    
	/**
	 * Saas查看个人资料(通用)
	 * @param request
	 * @param dealerId
	 * @return
	 */
	private ResponseMsg getPersionData(HttpServletRequest request, String dealerId) {
		ResponseMsg msg = new ResponseMsg();
		if(StringUtils.isBlank(dealerId)){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				dealerId = loginInfo.getDealerId();
			}
		}
		try {
			msg.setData(personalDataI.findRegister(dealerId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * @author felx
	 * 查询个人等级，等级积分，距离升级还需多少等级积分以及超过多少用户
	 * @param dealerId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/dealerRank","/app/dealerRank"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询个人等级，等级积分，距离升级还需多少等级积分以及超过多少用户", 
	notes = "查询个人等级，等级积分，距离升级还需多少等级积分以及超过多少用户")
	public ResponseMsg  findDealerRankAndRankScore(HttpServletRequest  req){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(req);
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
			msg.setData(personalDataI.findDealerRankAndRankScore(dealerId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 查询可用余额
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cashBack",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询可用余额", 
	notes = "查询可用余额")
	public ResponseMsg getDealerCashBack(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			String dealer = model.getDealerId(Channel.SAAS);
			msg.setData(personalDataI.getDealerCashBack(dealer));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 查询返现明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cashBackDetail/{page}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询返现明细")
	public ResponseMsg getDealerCashBackDetail(HttpServletRequest request,@PathVariable int page){
		int rows = 10;
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		String startTime  = model.getStringValue("startTime");//返现开始时间
		String endTime  = model.getStringValue("endTime");//返现结束时间
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		try {
			String dealer = model.getDealerId(Channel.SAAS);
			msg.setData(personalDataI.getDealerCashBackDetail(dealer,page*rows,rows,startTime,endTime));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 提交返现申请
	 * @param request
	 * @param cardId
	 * @param amount
	 * @return
	 */
	@RequestMapping(value="/toCashBack",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "提交返现申请", 
	notes = "提交返现申请")
	public ResponseMsg toCashBack(
			HttpServletRequest request,
			String cardId,//银行卡号
			String amount//返现金额
		){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			personalDataI.toCashBack(model);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}

	/**
	 * 用户银行卡
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/bankCard ",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "用户银行卡", 
	notes = "用户银行卡")
	public ResponseMsg getBankCard(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			String dealer = model.getDealerId(Channel.SAAS);
			msg.setData(personalDataI.getBankCard(dealer));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/ApplyDetail ",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "****")
	public ResponseMsg getApplyDetail(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			String dealer = model.getDealerId(Channel.SAAS);
			msg.setData(personalDataI.getApplyDetail(dealer));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * @author felx
	 * @describe 年度账单
	 * @param date 时间 格式 yyyy-mm
	 * @param loginInfo
	 * @return
	 */
	@RequestMapping(value="/annualBill",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "年度账单")
	@ResponseDefault
	@Login(saas="/annualBill",app="/annualBill")
	public Object getAnnualBill(String date,LoginInfoUtil loginInfo){
		return personalDataI.getAnnualBill(date,loginInfo.getDealerId());
	}
	
	
	
}
