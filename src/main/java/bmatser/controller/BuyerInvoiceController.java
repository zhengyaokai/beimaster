package bmatser.controller;
/**
 * 零售商发票
 */
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.model.DealerBean;
import bmatser.pageModel.BuyerInvoicePage;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.BuyerInvoiceI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
/**
 * 
 * @author felx
 *
 */
@Controller
@RequestMapping("invoice")
@Api(value="invoice", description="零售商发票")
public class BuyerInvoiceController {

	@Autowired
	private BuyerInvoiceI invoiceService;
	
	private String urlUpload = JFig.getInstance().getSection("system_options").get("URL_UPLOAD");
	/**
	 * 查询零售商发票(手机)
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/buyerid/{buyerId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询零售商发票(手机)", response = BuyerInvoicePage.class)
	public ResponseMsg findByBuyerId(@ApiParam(value="零售商Id")@PathVariable Integer buyerId,HttpServletRequest request){
		return getBuyerInvoiceList(buyerId,request);
	}
	/**
	 * 查询零售商发票(Saas)
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/buyerid", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询零售商发票(Saas)", response = BuyerInvoicePage.class)
	public ResponseMsg findByBuyerId(HttpServletRequest request){

	    return getBuyerInvoiceList(null,request);
	}
	
	/**
	 * 设置默认发票
	 * @param id
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/setdefault/{id}/{buyerId}", method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "设置默认发票")
	public ResponseMsg setDefault(@ApiParam(value="发票id")@PathVariable Integer id,@ApiParam(value="零售商Id") @PathVariable Integer buyerId,HttpServletRequest request){
		return setDefaultInvoice(id,buyerId,request);
	}
	/**
	 * 设置默认发票(手机)
	 * @param id
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/mobile/setdefault", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "设置默认发票(手机)" )
	public ResponseMsg setDefault(@ApiParam(value="发票id")@RequestParam(required=false)String id,
			@ApiParam(value="零售商Id")@RequestParam(required=false)String buyerId,HttpServletRequest request){
		return setDefaultInvoice(id!=null?Integer.parseInt(id):null,buyerId!=null?Integer.parseInt(buyerId):null,request);
	}
	/**
	 * 设置默认发票(Saas)
	 * @param id
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/setdefault", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "设置默认发票(Saas)")
	public ResponseMsg setSaasDefault(@ApiParam(value="发票id")@RequestParam(required=false)String id,HttpServletRequest request){
		return setDefaultInvoice(id!=null?Integer.parseInt(id):null,null,request);
	}

	/**
	 * 新增发票
	 * @param invoicePage
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "新增发票")
	public ResponseMsg add(@ModelAttribute BuyerInvoicePage invoicePage,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		String dealerName = "";
		if(invoicePage.getBuyerId()==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				invoicePage.setBuyerId(Integer.parseInt(loginInfo.getDealerId()));
				dealerName = loginInfo.getDealerName();
			}
		}
		try {
			invoicePage.setDealerName(dealerName);
			msg.setData(invoiceService.add(invoicePage,null,null,null));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 新增发票
	 * @param invoicePage
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/form")
	@ApiOperation(value = "新增发票")
	public ModelAndView  addForm(@ModelAttribute BuyerInvoicePage invoicePage,
			HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="authorImg",required = false) MultipartFile authorImg,
			@RequestParam(value="licenseFile",required = false) MultipartFile licenseFile,
			@RequestParam(value="invApplicationFile",required = false) MultipartFile invApplicationFile){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			LoginInfo login = model.getSaasLogin();
			invoicePage.setDealerName(login.getDealerName());
			invoicePage.setBuyerId(Integer.parseInt(login.getDealerId()));
			invoiceService.add(invoicePage,authorImg,licenseFile,invApplicationFile);
			return new ModelAndView("redirect:"+urlUpload+"/customer/invoice/info");//"redirect:"+urlUpload+"/customer/invoice/info"
		} catch (Exception e) {
			msg.setError(e);
			out.print("<script>alert('"+msg.getMsg()+"');window.location.href='"+urlUpload+"/customer/invoice/info?isAdd=2';</script>");
			out.flush();
			return null;
		}
	}
	/**
	 * 修改发票(旧版)
	 * @param invoicePage
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "修改发票(旧版)")
	public ResponseMsg edit(@ModelAttribute BuyerInvoicePage invoicePage,HttpServletRequest request) {
		return editInv(invoicePage,request);
	}
	/**
	 * 修改发票(手机)
	 * @param invoicePage
	 * @return
	 */
	@RequestMapping(value="/mobile/edit",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改发票(手机)")
	public ResponseMsg editByMob(@ModelAttribute BuyerInvoicePage invoicePage,HttpServletRequest request) {
		return editInv(invoicePage,request);
	}
	/**
	 * 修改发票(saas)
	 * @param invoicePage
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改发票(saas)", response = BuyerInvoicePage.class)
	public ResponseMsg editBySaas(@ModelAttribute BuyerInvoicePage invoicePage,HttpServletRequest request) {
		return editInv(invoicePage,request);
	}
	/**
	 * 修改发票(saas)
	 * @param invoicePage
	 * @return
	 */
	@RequestMapping(value="/edit/form",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改发票(saas)", response = BuyerInvoicePage.class)
	public ModelAndView editBySaas(@ModelAttribute BuyerInvoicePage invoicePage,HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="authorImg",required = false) MultipartFile authorImg,
			@RequestParam(value="licenseFile",required = false) MultipartFile licenseFile,
			@RequestParam(value="invApplicationFile",required = false) MultipartFile invApplicationFile) {
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			LoginInfo login = model.getSaasLogin();
			invoicePage.setBuyerId(Integer.parseInt(login.getDealerId()));
			invoicePage.setDealerName(login.getDealerName());
			invoiceService.edit(invoicePage,authorImg,licenseFile,invApplicationFile);
			return new ModelAndView("redirect:"+urlUpload+"/customer/invoice/info");//"redirect:"+urlUpload+"/customer/invoice/info"
		} catch (Exception e) {
			msg.setError(e);
			out.print("<script>alert('"+msg.getMsg()+"');window.location.href='"+urlUpload+"/customer/invoice/info?isAdd=2';</script>");
			out.flush();
			return null;
		}
	}
	/**
	 * 删除发票(旧版)
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "删除发票(旧版)")
	public ResponseMsg delete(@ApiParam(value="发票id") @PathVariable Integer id){
		ResponseMsg msg = new ResponseMsg();
		try {
			int  i = invoiceService.delete(id);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setMsg("删除失败");
				msg.setCode(-1);
			}
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	/**
	 * 删除发票(手机)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/mobile/del", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除发票(手机)")
	public ResponseMsg delete(@ApiParam(value="发票id")@RequestParam(required=false)String id,
			@ApiParam(value="buyerId")@RequestParam(required=false)String buyerId,HttpServletRequest request){

		return deleteInvoice(id,buyerId,request);
	}
	/**
	 * 删除发票(Saas)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/del", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除发票(Saas)", response = BuyerInvoicePage.class)
	public ResponseMsg deleteInv(@ApiParam(value="发票id")@RequestParam(required=false)String id,HttpServletRequest request){
		return deleteInvoice(id,null,request);
	}
	
	/**
	 * 获取默认发票
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/default/{buyerId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "手机获取默认发票", response = BuyerInvoicePage.class)
	public Map getDefaultByBuyerId(@ApiParam(value="零售商Id")@PathVariable Integer buyerId) {
		return invoiceService.getDefaultByBuyerId(buyerId);
	}
	/**
	 * 获取默认发票
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/default", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas获取默认发票", response = BuyerInvoicePage.class)
	public ResponseMsg getDefault(HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			msg.setData(invoiceService.getDefaultByBuyerId(Integer.parseInt(loginInfo.getDealerId())));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 获取发票详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/id/{id}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "手机获取发票详情", response = BuyerInvoicePage.class)
	public ResponseMsg findInvoiceById(@ApiParam(value="发票id")@PathVariable Integer id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(invoiceService.findInvoiceById(id));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 获取发票详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/id", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas获取发票详情", response = BuyerInvoicePage.class)
	public ResponseMsg findInvoiceById(@ApiParam(value="发票id")@RequestParam String id,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		/*String id = request.getParameter("id");*/
		try {
			msg.setData(invoiceService.findInvoiceById(Integer.parseInt(id)));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	
	/**
	 * 商城获取默认发票
	 * @author felx
	 */
	@RequestMapping(value="/mallInvoice", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城获取默认发票", response = BuyerInvoicePage.class)
	public ResponseMsg findMallInvoice(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			Map map= invoiceService.getDefaultByBuyerId(Integer.valueOf(loginInfo.getDealerId()));
			if(!map.isEmpty()){
				msg.setData(map);
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	
	/**
	 * 商城个人中心获取发票列表
	 * @author felx
	 */
	@RequestMapping(value="/getMallInvoiceList", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城个人中心获取发票列表", response = BuyerInvoicePage.class)
	public ResponseMsg getMallInvoiceList(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			Map map= invoiceService.getMallInvoiceList(Integer.valueOf(loginInfo.getDealerId()));
			if(!map.isEmpty()){
				msg.setData(map);
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	
	 /** APP商城个人中心获取发票列表
	 * @author felx
	 */
	@RequestMapping(value="/app/getMallInvoiceList", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "APP商城个人中心获取发票列表", response = BuyerInvoicePage.class)
	public ResponseMsg getAppMallInvoiceList(@ApiParam(value="零售商id") @RequestParam Integer dealerId){
		ResponseMsg msg = new ResponseMsg();
		try {
			Map map= invoiceService.getMallInvoiceList(dealerId);
			if(!map.isEmpty()){
				msg.setData(map);
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}	
	
	/**
	 * 商城设置默认发票
	 * @author felx
	 */
	@RequestMapping(value="/setdefault/{id}", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城设置默认发票", response = BuyerInvoicePage.class)
	public ResponseMsg setMallDefault(@ApiParam(value="发票id") @PathVariable Integer id, HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		int count = invoiceService.setDefault(id, Integer.valueOf(loginInfo.getDealerId()));
		if(count > 0){
			msg.setCode(0);
		}else{
			msg.setCode(-1);
		}
		return msg;
	}
	
	/**
	 * 商城新增发票
	 * @author felx
	 */
	@RequestMapping(value="/addInvoice",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城新增发票")
	public ResponseMsg addInvoice(@ModelAttribute BuyerInvoicePage invoicePage,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}else{
			invoicePage.setBuyerId(Integer.valueOf(loginInfo.getDealerId()));
			if(null!=invoicePage.getInvType() && "1".equals(invoicePage.getInvType())){
				//普票不需要设置账户信息
				invoicePage.setRecvName("");
				invoicePage.setRecvMobile("");
				invoicePage.setRecvProvince(0);
				invoicePage.setRecvCity(0);
				invoicePage.setRecvArea(0);
				invoicePage.setRecvAddress("");
				invoicePage.setInvContent("");
				if(null!=invoicePage.getInvTitleType() && "1".equals(invoicePage.getInvTitleType())){
					invoicePage.setInvTitle(loginInfo.getDealerName());
				}
			}
		}
		try {
			msg.setData(invoiceService.addInvoice(invoicePage));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * APP商城新增发票
	 * @author felx
	 */
	@RequestMapping(value="/addAppMallInvoice",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "APP商城新增发票")
	public ResponseMsg addAppMallInvoice(@ModelAttribute BuyerInvoicePage invoicePage,@ApiParam(value="零售商id") @RequestParam String dealerId,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		/*LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}else{*/
			invoicePage.setBuyerId(Integer.valueOf(dealerId));
			if(null!=invoicePage.getInvType() && "1".equals(invoicePage.getInvType())){
				//普票不需要设置账户信息
				invoicePage.setRecvName("");
				invoicePage.setRecvMobile("");
				invoicePage.setRecvProvince(0);
				invoicePage.setRecvCity(0);
				invoicePage.setRecvArea(0);
				invoicePage.setRecvAddress("");
				invoicePage.setInvContent("");
				if(null!=invoicePage.getInvTitleType() && "1".equals(invoicePage.getInvTitleType())){
					invoicePage.setInvTitle(invoiceService.findById(dealerId).get("dealerName").toString());
				}
			}
		
		try {
			msg.setData(invoiceService.addInvoice(invoicePage));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
			e.printStackTrace();
		}
		return msg;
	}
	
	
	/**
	 * 修改发票
	 * @param invoicePage
	 * @return
	 */
	@RequestMapping(value="/editInvoice",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "修改发票")
	public ResponseMsg editInvoice(@ModelAttribute BuyerInvoicePage invoicePage,HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}else{
			invoicePage.setBuyerId(Integer.valueOf(loginInfo.getDealerId()));
		}
		try {
			invoiceService.edit(invoicePage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	
	/**
	 * 商城删除发票
	 * @param id
	 * @param buyerId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/deleteInvoiceMall",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城删除发票", response = BuyerInvoicePage.class)
	public ResponseMsg deleteInvoiceMall(@ApiParam(value="发票id")@RequestParam Integer id, HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		try {
			int  i = invoiceService.delete(id);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setMsg("删除失败");
				msg.setCode(-1);
			}
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}

	
	/**
	 * 查询零售商发票(通用)
	 * @param buyerId
	 * @param request
	 * @return
	 */
	private ResponseMsg getBuyerInvoiceList(Integer buyerId,
			HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		if(buyerId==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = Integer.parseInt(loginInfo.getDealerId());
			}
		}
		try {
			msg.setData(invoiceService.findByBuyerId(buyerId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 设置默认发票(通用)
	 * @param id
	 * @param buyerId
	 * @param request 
	 * @return
	 */
	private ResponseMsg setDefaultInvoice(Integer id, Integer buyerId, HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		if(buyerId==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = Integer.parseInt(loginInfo.getDealerId());
			}
		}
		try {
			invoiceService.setDefault(id, buyerId);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 修改发票(通用)
	 * @param invoicePage
	 * @param request
	 * @return
	 */
	private ResponseMsg editInv(BuyerInvoicePage invoicePage, HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		if(invoicePage.getBuyerId()==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				invoicePage.setBuyerId(Integer.parseInt(loginInfo.getDealerId()));
			}
		}
		try {
			invoiceService.edit(invoicePage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 删除发票(通用)
	 * @param id
	 * @param buyerId
	 * @param request
	 * @return
	 */
	private ResponseMsg deleteInvoice(String id, String buyerId,
			HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		if(StringUtils.isBlank(buyerId)){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = loginInfo.getDealerId();
			}
		}
		try {
			int  i = invoiceService.delete(id,buyerId);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setMsg("删除失败");
				msg.setCode(-1);
			}
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	
	/**
	 * 判断会员是否在专票白名单中
	 * @param id
	 * @param buyerId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/selectIsWhite", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "判断会员是否在专票白名单中")
	public ResponseMsg selectIsWhite(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			msg.setData(invoiceService.selectIsWhite(Integer.valueOf(loginInfo.getDealerId())));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 新增专票 上传申请书
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/addApplication")
	@ApiOperation(value = "新增专票 上传申请书")
	public ResponseMsg  addApplication(
			HttpServletRequest request,
			@RequestParam(value="invApplicationFile",required = false) MultipartFile invApplicationFile
			){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		try {
			invoiceService.addApplication(pageMode.getSaasLogin().getDealerId(),invApplicationFile);
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 发票明细列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value={"/getInvoiveDetialList/{page}","/app/getInvoiveDetialList/{page}"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "发票明细列表")
	public ResponseMsg getInvoiveDetialList(@ApiParam(value="页数")  @PathVariable Integer page,
			@ApiParam(value="订单号") @RequestParam(required = false) Long orderId,
			@ApiParam(value="发票号") @RequestParam(required = false) String fjsNo,
			HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		PageMode pageMode = new PageMode(request);
		Integer dealerId;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
			msg.setData(invoiceService.getInvoiveDetialList(dealerId,orderId,fjsNo,page*10, 10));
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
}
