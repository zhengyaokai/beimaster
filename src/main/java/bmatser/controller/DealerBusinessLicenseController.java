package bmatser.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import bmatser.annotations.Login;
import bmatser.annotations.ResponseDefault;
import bmatser.model.Base64DecodeMultipartFile;
import bmatser.model.DealerBusinessLicense;
import bmatser.model.LoginInfoUtil;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.DealerBusinessLicenseI;
import bmatser.service.RegisterI;
import bmatser.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import net.rubyeye.xmemcached.MemcachedClient;
import org.apache.commons.fileupload.FileItem;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 营业执照
 * @author felx
 */
@Controller
@RequestMapping("dealerBusinessLicense")
@Api(value="dealerBusinessLicense", description="营业执照")
public class DealerBusinessLicenseController {
	
	private String urlUpload = (String) JFig.getInstance().getSection("system_options").get("URL_UPLOAD");
	private String wxUrl = (String) JFig.getInstance().getSection("system_options").get("WX_URL");
	@Autowired
	private DealerBusinessLicenseI dealerBusinessLicenseI;
	@Autowired
	private MemcachedClient memcachedClient;
	@Autowired
	private RegisterI registerService;
	/**
	 * 查询营业执照关联信息
	 * @param HttpServletRequest request,Map mp
	 * @return 
	 */
	@RequestMapping(value="/select", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询营业执照关联信息", response = DealerBusinessLicense.class)
	ResponseMsg select(@ApiParam(value="dealerId") @RequestParam(required=false)String dealerId){
		int rows = 10;
		ResponseMsg res = new ResponseMsg();
		if(null == dealerId||"".equals(dealerId)){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			List strList= dealerBusinessLicenseI.selectByDealerId(dealerId);
			res.setData(strList);
			res.setCode(0);
		} catch (Exception e) {
			res.setMsg(e.toString());
			res.setCode(-1);
		}
		return res;
	}
	/**
	 * 手机端新增营业执照
	 * @param HttpServletRequest request,MemberFavorite memberFavorite
	 * @return 
	 */
//	@RequestMapping(value="/add",method=RequestMethod.POST)
//	@ResponseBody
//	@ApiOperation(value = "手机端新增营业执照")
//	@Deprecated
//	public ResponseMsg add(@ModelAttribute DealerBusinessLicense dealerBusinessLicense,
//			@ModelAttribute Dealer dealer,
//			@ModelAttribute CrmCustomer crmCustomer,HttpServletRequest request)
//			/*,@RequestParam(value="uploadFile",required = false) MultipartFile file,
//			@RequestParam(value="uploadFileTwo",required = false) MultipartFile fileTwo,
//			@RequestParam(value="uploadFileThree",required = false) MultipartFile fileThree)*/{
//		ResponseMsg res = new ResponseMsg();
//		if(null == dealerBusinessLicense.getDealerId()){
//			return res.setError("请先登录");
//		}
///*		MultipartFile []files=new MultipartFile[3];
//			files[0]=file;
//        	files[1]=fileTwo;
//        	files[2]=fileThree;*/
//		try {
//			String staffNo=StringUtils.isBlank(request.getParameter("staffNo"))?"":request.getParameter("staffNo");
//			int i = dealerBusinessLicenseI.insert(Constants.mobileIs,staffNo,dealerBusinessLicense.getDealerId(),dealerBusinessLicense,null,dealer,crmCustomer,1);
//			if(i>0){
//				res.setData(i);//返回状态    modify 20160622   审核状态（0：待审核 1：审核通过 上架 2：审核不通过 3：已提交资料，未审核 下架）
//				res.setCode(0);
//				res.setMsg("认证成功");
//			}else{
//				res.setCode(-1);
//				res.setError("认证失败");
//			}
//		} catch (Exception e) {
//			res.setError(e);
//		}
//		return res;
//	}
	
	


    /**
     * create by yr on 2018-11-08
     */

    @RequestMapping(value="/addAppForIos",method=RequestMethod.POST)
    @ResponseBody
    @Login(app="/addAppForIos")
  public ResponseMsg addAppForIos(HttpServletRequest request,
                          DealerBusinessLicense license,
                         @RequestParam(value="uploadFile",required = false) String  file //营业执照号
                        ,LoginInfoUtil login) {
        ResponseMsg msg = new ResponseMsg();
        StringBuffer sb = new StringBuffer("data:image/jpg;base64,").append(file);
        MultipartFile newFile =  base64Convert(sb.toString());
        try{
            dealerBusinessLicenseI.insert(license,newFile,null,null,"ios");
            msg.setCode(0);
            msg.setData("资料提交成功");
        }catch(Exception e){
            msg.setData(e.getMessage());
            msg.setCode(-1);
        }
        return msg;
    }


        public static MultipartFile base64Convert(String base64) {
            String[] baseStrs = base64.split(",");
            BASE64Decoder decoder = new BASE64Decoder();
             byte[] b = new byte[0];
             try {
                    b = decoder.decodeBuffer(baseStrs[1]);
                 } catch (IOException e) {
                         e.printStackTrace();
                 }
            for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                            b[i] += 256;
                 }
            }
                return new Base64DecodeMultipartFile(b, baseStrs[0]);
        }
        /**
         * PC端新增营业执照
         * @param request
         * @param license
         * @param file 营业执照号
         * @param fileTwo 税务登记证
         * @param fileThree 组织机构代码证
         * @return ModelAndView
         */
	@RequestMapping(value="/addPC",method=RequestMethod.POST)
	@ApiOperation(value = "PC端新增营业执照")
	public ModelAndView addPc(HttpServletRequest request,
			@ModelAttribute DealerBusinessLicense license,
			@RequestParam(value="uploadFile",required = false) MultipartFile file,//营业执照号
			@RequestParam(value="uploadFileTwo",required = false) MultipartFile fileTwo,//税务登记证
			@RequestParam(value="uploadFileThree",required = false) MultipartFile fileThree//组织机构代码证
		){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			Integer dealerId = model.getDealerIdDecimalVal(Channel.SAAS).intValue();
			license.setDealerId(dealerId);
			dealerBusinessLicenseI.insert(license,file,fileTwo,fileThree,null);
			registerService.loadUserInfo(request);
		} catch (Exception e) {
			msg.setError(e);
			try {
				return new ModelAndView("redirect:"+urlUpload+"/register/complete")
				.addObject("msg",URLEncoder.encode(msg.getMsg(),"utf-8"));
			} catch (Exception e2) {
				e2.printStackTrace();
				return new ModelAndView("redirect:"+urlUpload+"/register/complete");
			}
		}
		return new ModelAndView("redirect:"+urlUpload+"/register/verify");
	}
	/**
	 * wx端新增营业执照
	 * @param request
	 * @param license
	 * @param file 营业执照号
	 * @param fileTwo 税务登记证
	 * @param fileThree 组织机构代码证
	 * @return ModelAndView
	 */
//	@RequestMapping(value="/addWx",method=RequestMethod.POST)
//	@ApiOperation(value = "wx端新增营业执照")
//	public ModelAndView addWx(HttpServletRequest request,
//			@ModelAttribute DealerBusinessLicense license,
//			@RequestParam(value="uploadFile",required = false) MultipartFile file,//营业执照号
//			@RequestParam(value="uploadFileTwo",required = false) MultipartFile fileTwo,//税务登记证
//			@RequestParam(value="uploadFileThree",required = false) MultipartFile fileThree//组织机构代码证
//			){
//		ResponseMsg msg = new ResponseMsg();
//		PageMode model = new PageMode(request);
//		try {
//			Integer dealerId = model.getDealerIdDecimalVal(Channel.SAAS).intValue();
//			license.setDealerId(dealerId);
//			dealerBusinessLicenseI.insert(license,file,fileTwo,fileThree);
//			registerService.loadUserInfo(request);
//		} catch (Exception e) {
//			msg.setError(e);
//			try {
//				return new ModelAndView("redirect:"+wxUrl+"/account/registerMember")
//				.addObject("msg",URLEncoder.encode(msg.getMsg(),"utf-8"));
//			} catch (Exception e2) {
//				e2.printStackTrace();
//				return new ModelAndView("redirect:"+wxUrl+"/account/registerMember");
//			}
//		}
//		return new ModelAndView("redirect:"+wxUrl+"/account/registerExamine");
//	}
	
	
	
	
/*	*//**
	 * PC端新增营业执照
	 * @param HttpServletRequest request,MemberFavorite memberFavorite
	 * @return 
	 *//*
	@RequestMapping(value="/addPC",method=RequestMethod.POST)
	@ApiOperation(value = "PC端新增营业执照")
	@Deprecated
	public ModelAndView addPC(HttpServletRequest request,@ModelAttribute DealerBusinessLicense dealerBusinessLicense,
			@ModelAttribute Dealer dealer,
			@ModelAttribute CrmCustomer crmCustomer,
			@RequestParam(value="uploadFile",required = false) MultipartFile file,
			@RequestParam(value="uploadFileTwo",required = false) MultipartFile fileTwo,
			@RequestParam(value="uploadFileThree",required = false) MultipartFile fileThree,HttpServletResponse response){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		String urlUpload="";
		try {
			urlUpload = JFig.getInstance().getValue("system_options", "URL_UPLOAD");
		} catch (Exception e2) {
			e2.printStackTrace();
			return new ModelAndView();
		}
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			try {
				return new ModelAndView("redirect:"+urlUpload+"page/login").addObject("msg",URLEncoder.encode(res.getMsg(),"utf-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MultipartFile []files=new MultipartFile[3];
			files[0]=file;
        	files[1]=fileTwo;
        	files[2]=fileThree;
        	String staffNo=StringUtils.isBlank(request.getParameter("staffNo"))?"":request.getParameter("staffNo");
        	if(StringUtils.isNotBlank(staffNo)){
        		Pattern pn=Pattern.compile("^(?![0-9]+$)(?![a-z]+$)[0-9a-z]{6}$");
            	Matcher mr=pn.matcher(staffNo);
            	if(mr.matches()==false){
            		res.setCode(-1);
            		res.setMsg("非6位数字与小写字母组合");
        			try {
						return new ModelAndView("redirect:"+urlUpload+"/register/authen").addObject("msg",URLEncoder.encode(res.getMsg(),"utf-8"));
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}
        	}
        	try {
        		dealer.setDealerName(URLDecoder.decode(null==dealer.getDealerName()?"":dealer.getDealerName(),"utf-8"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		try {
			int i = dealerBusinessLicenseI.insert(Constants.PcIs,staffNo,Integer.parseInt(loginInfo.getDealerId()),dealerBusinessLicense,files,dealer,crmCustomer,0);
			registerService.loadUserInfo(request);
			if(i>0){
				if(i==1){
					return new ModelAndView("redirect:"+urlUpload+"/register/success");//有邀请码
				}else if(i==3){
					return new ModelAndView("redirect:"+urlUpload+"/register/verify");//无邀请码
				}
			}else{
				res.setCode(-1);
				res.setMsg("认证失败");
				return new ModelAndView("redirect:"+urlUpload+"/register/authen").addObject("msg",URLEncoder.encode(res.getMsg(),"utf-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(-1);
			res.setMsg(e.getMessage()==null?"上传审核失败，请联系客服!":e.getMessage());
			try {
				return new ModelAndView("redirect:"+urlUpload+"/register/authen").addObject("msg",URLEncoder.encode(res.getMsg(),"utf-8"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return new ModelAndView();
	}*/
	/**
	 * 显示营业执照  
	 * @param 
	 * @return 
	 */
	@RequestMapping(value="/showPic",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "显示营业执照 ")
	public ResponseMsg selectShowPic(HttpServletRequest request,HttpServletResponse response){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null != loginInfo && null != loginInfo.getDealerId()){
		try {
			String key="tempImg-"+loginInfo.getDealerId();
			CachedImage.selectImage(request, response, memcachedClient, key);
		} catch (Exception e) {
			res.setCode(-1);
			res.setMsg("显示图片失败");
			res.setData(e.getMessage());
			return res;
		}
		res.setCode(0);
		res.setMsg("显示图片成功");
		return res;
         }else{
        	res.setCode(-1);
 			res.setMsg("显示图片失败");
 			return res;
         }
		}
	
	/**
	 * 保存营业执照 
	 * @param 
	 * @return 
	 */
	@RequestMapping(value="/savePic",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存营业执照 ")
	public ResponseMsg savePic(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value="uploadFile",required = false) MultipartFile file){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null != loginInfo && null != loginInfo.getDealerId()){
		if(null!=file){
         	try {     
					if(FileTypeUtil.isImage(file.getInputStream())){
						String key="tempImg-"+loginInfo.getDealerId();
						CachedImage.insertImage(memcachedClient, file, key);
					    }else{
					    	 res.setCode(-1);
						     res.setMsg("不是图片");
						     return res;
					    }
					}
				 catch (Exception e) {
					 res.setCode(-1);
				     res.setMsg("失败");
				     res.setData(e.getMessage());
				     return res;
				}
         	res.setCode(0);
			res.setMsg("成功");
			return res;
        }else{
        	res.setCode(-1);
		    res.setMsg("失败");
		    res.setData("没有图片");
		    return res;
        }
		}else{
				res.setCode(-1);
				res.setMsg("失败");
				 res.setData("没有用户信息");
				 return res;
		}
    }
	
	@RequestMapping(value="/isRepeat",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="验证公司名称是否存在")
	public ResponseMsg isRepeatDealerName(
			@ApiParam("公司名称") @RequestParam String dealerName,
			HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			String dealerId = model.getDealerId(Channel.SAAS);
			dealerBusinessLicenseI.isRepeatDealerName(dealerName,dealerId);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 审核失败理由
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkReason",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="审核失败理由")
	public ResponseMsg checkReason(
			HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			String dealerId = model.getDealerId(Channel.SAAS);
			msg.setData(dealerBusinessLicenseI.checkReason(dealerId));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 审核失败理由和填写的信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value={"/app/checkInfo"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="审核失败理由和填写的信息")
	@Login(app="/app/checkInfo")
	@ResponseDefault
	public Object checkInfo(LoginInfoUtil login){
		return dealerBusinessLicenseI.checkInfo(login.getDealerId());
	}
	
	/**
	 * @author felx
	 * @describe 注册渠道
	 * @return
	 */
	@RequestMapping(value={"/regChannel"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="审核失败理由和填写的信息")
	@Login(saas="/regChannel")
	@ResponseDefault
	public Object getRegChannel(LoginInfoUtil login){
		return dealerBusinessLicenseI.getRegChannel(login.getDealerId());
	}
	
	
	
	
}