package bmatser.controller;
/**
 * 零售商地址
 */
import bmatser.dao.OrderGoodsMapper;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.model.ApplicationApp;
import bmatser.pageModel.BuyerConsignAddressPage;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.BuyerConsignAddressI;
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
@RequestMapping("receive_address")
@Api(value="receiveAddress", description="零售商地址")
public class BuyerConsignAddressController {

	@Autowired
	private BuyerConsignAddressI consignAddressService;

    @Autowired
    private OrderGoodsMapper orderGoodsDao;
	/**
	 * 查询零售商收货地址
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/customer/{buyerId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "手机查询零售商收货地址", response = BuyerConsignAddressPage.class)
	ResponseMsg findByBuyerId(@ApiParam(value="零售商Id") @PathVariable Integer buyerId,HttpServletRequest request){
		return getBuyerConsignAddress(buyerId,request);
	}
	/**
	 * 查询零售商收货地址
	 * @param buyerId
	 * @return
	 */	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "pc查询零售商收货地址", response = BuyerConsignAddressPage.class)
	public ResponseMsg findAll(HttpServletRequest request){
		return getBuyerConsignAddress(null,request);
	}

	
//	@RequestMapping(value="/jf", method=RequestMethod.GET,produces="text/html;charset=UTF-8")
//	@ResponseBody
//	@ApiOperation(value = "积分商城查询零售商收货地址", response = BuyerConsignAddressPage.class)
//	public String findAll(HttpServletRequest request,@ApiParam(value="jsonp返回")@RequestParam String callback){
//		ResponseMsg msg = new ResponseMsg();
//		msg = getBuyerConsignAddress(null,request);
//		return callback + "(" + JSONUtil.objectToJson(msg).toString() + ")";
//	}


	/**
	 * 设置默认地址
	 * @param id
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/setdefault/{id}/{buyerId}", method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "pc设置默认地址")
	public ResponseMsg setDefault(@ApiParam(value="收货地址id") @PathVariable Integer id,@ApiParam(value="零售商Id") @PathVariable Integer buyerId,HttpServletRequest request){
		return setdefaultAdress(id,buyerId,request);
	}
	/**
	 * 设置默认地址(手机版)
	 * @param id
	 * @param buyerId
	 * @return
	 */	
//	@RequestMapping(value="/mobile/setdefault", method=RequestMethod.POST)
//	@ResponseBody
//	@ApiOperation(value = "手机版设置默认地址")
//	public ResponseMsg setDefaultByMoblie(@ApiParam(value="收货地址id")@RequestParam(required=false) String id,
//			@ApiParam(value="零售商Id")@RequestParam(required=false) String buyerId,HttpServletRequest request){
//		return setdefaultAdress(id!=null?Integer.parseInt(id):null,buyerId!=null?Integer.parseInt(buyerId):null,request);
//	}
	/**
	 * 设置默认地址(Saas版)
	 * @param id
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/setdefault", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "saas设置默认地址")
	public ResponseMsg setDefaultBySaas(@ApiParam(value="收货地址id")@RequestParam(required=false) String id,HttpServletRequest request){
		return setdefaultAdress(id!=null?Integer.parseInt(id):null,null,request);
	}

	/**
	 * 保存收货地址
	 * @param consignAddressPage
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存收货地址")
	public ResponseMsg addBuyerConsignAddress(@ModelAttribute BuyerConsignAddressPage consignAddressPage,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(consignAddressPage.getBuyerId()==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				consignAddressPage.setBuyerId(Integer.parseInt(loginInfo.getDealerId()));
			}
		}
		try {
			msg.setData(consignAddressService.add(consignAddressPage));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 修改收货地址
	 * @param consignAddressPage
	 * @return
	 */
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改收货地址")
	public ResponseMsg editBuyerConsignAddress(@ModelAttribute BuyerConsignAddressPage consignAddressPage,HttpServletRequest request){
		return editAddress(consignAddressPage,request);
	}

	/**
	 * 修改收货地址(手机版)
	 * @param consignAddressPage
	 * @return
	 */
//	@RequestMapping(value="/mobileEdit",method=RequestMethod.POST)
//	@ResponseBody
//	@ApiOperation(value = "修改收货地址(手机版)")
//	public ResponseMsg editByMobile(@ModelAttribute BuyerConsignAddressPage consignAddressPage,HttpServletRequest request){
//		return editAddress(consignAddressPage,request);
//	}
	/**
	 * 删除地址
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "删除地址")
	public ResponseMsg delete(@ApiParam(value="收货地址id")@RequestParam(required=false) String id,
			@ApiParam(value="零售商Id")@RequestParam(required=false) String buyerId,HttpServletRequest request){
		return delAddress(id,buyerId,request);
	}
	/**
	 * 删除地址(手机版)
	 * @param request
	 * @return
	 */
//	@RequestMapping(value="/mobile/del", method=RequestMethod.POST)
//	@ResponseBody
//	@ApiOperation(value = "删除地址(手机版)")
//	public ResponseMsg deleteAddress(@ApiParam(value="收货地址id")@RequestParam(required=false) String id,
//			@ApiParam(value="零售商Id")@RequestParam(required=false) String buyerId,HttpServletRequest request){
//		return delAddress(id,buyerId,request);
//	}
	/**
	 * 删除地址(Saas版)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saas/del", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除地址(Saas版)")
	public ResponseMsg deleteSaasAddress(@ApiParam(value="收货地址id")@RequestParam(required=false) String id,HttpServletRequest request){
		return delAddress(id,null,request);
	}

	/**
	 * 获得默认地址(saas)
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/default", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获得默认地址(saas)", response = BuyerConsignAddressPage.class)
	public ResponseMsg getDefaultByBuyerId(HttpServletRequest request) {
		return getDefault(null,request);
	}
	/**
	 * 获得默认地址(手机)
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/default/{buyerId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获得默认地址(手机)", response = BuyerConsignAddressPage.class)
	public ResponseMsg getDefaultByBuyerId(@ApiParam(value="零售商Id")@PathVariable Integer buyerId,HttpServletRequest request) {
		return getDefault(buyerId,request);
	}
	
	/**
	 * 获得单个地址明细
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/id/{id}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "手机获得单个地址明细", response = BuyerConsignAddressPage.class)
	public ResponseMsg getConsignAddressById(@ApiParam(value="收货地址id")@PathVariable Integer id){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(consignAddressService.getConsignAddressById(id));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 获得单个地址明细
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/id", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas获得单个地址明细", response = BuyerConsignAddressPage.class)
	public ResponseMsg getConsignAddressById(@ApiParam(value="收货地址id")@RequestParam(required=false)String id,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){

			return msg.setError("请先登录");
		}
		/*String id = request.getParameter("id");*/
		try {
			msg.setData(consignAddressService.getConsignAddressById(Integer.parseInt(id)));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城查询会员收货地址
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/mallAddress", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城查询会员收货地址", response = BuyerConsignAddressPage.class)
	ResponseMsg findMallAddress(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			List<BuyerConsignAddressPage> list= consignAddressService.findByBuyerId(Integer.valueOf(loginInfo.getDealerId()));
			msg.setData(list);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城查询会员收货地址
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/mallAddr", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城查询会员收货地址", response = BuyerConsignAddressPage.class)
	ResponseMsg findMallAddr(HttpServletRequest request,String ids){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			Integer dealer = model.getDealerIdDecimalVal(Channel.MALL).intValue();
			msg.setData(consignAddressService.findByBuyerId(dealer,ids));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城设置默认收货地址
	 * @author felx
	 */
	@RequestMapping(value="/setdefault/{id}", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城设置默认收货地址")
	public ResponseMsg setDefault(@ApiParam(value="收货地址id")@PathVariable Integer id, HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			consignAddressService.setDefault(id, Integer.valueOf(loginInfo.getDealerId()));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 商城保存收货地址
	 * @param consignAddressPage
	 * @return
	 */
	@RequestMapping(value="/addAddress", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城保存收货地址")
	public ResponseMsg add(@ModelAttribute BuyerConsignAddressPage consignAddressPage,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}else{
			consignAddressPage.setBuyerId(Integer.valueOf(loginInfo.getDealerId()));
		}
		try {
			msg.setData(consignAddressService.add(consignAddressPage));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 商城修改收货地址
	 * @param consignAddressPage
	 * @return
	 */
	@RequestMapping(value="/editAddress",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城修改收货地址")
	public ResponseMsg edit(@ModelAttribute BuyerConsignAddressPage consignAddressPage,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}else{
			consignAddressPage.setBuyerId(Integer.valueOf(loginInfo.getDealerId()));
		}
		try {
			consignAddressService.edit(consignAddressPage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城删除收货地址
	 * @author felx
	 */
	@RequestMapping(value="/delAddress/{id}", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城删除收货地址")
	public ResponseMsg delAddress(@ApiParam(value="收货地址id") @PathVariable Integer id,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			consignAddressService.delete(id);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 修改地址(通用)
	 * @param consignAddressPage
	 * @return
	 */
	private ResponseMsg editAddress(BuyerConsignAddressPage consignAddressPage, HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		if(consignAddressPage.getBuyerId()==null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				consignAddressPage.setBuyerId(Integer.parseInt(loginInfo.getDealerId()));
			}
		}
		try {
			int i = consignAddressService.edit(consignAddressPage);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setError("修改失败");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 设置默认地址(通用)
	 * @param request 
	 * @param consignAddressPage
	 * @return
	 */
	private ResponseMsg setdefaultAdress(Integer id, Integer buyerId, HttpServletRequest request) {
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
			consignAddressService.setDefault(id, buyerId);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 删除地址(通用)
	 * @param request 
	 * @param consignAddressPage
	 * @return
	 */
	private ResponseMsg delAddress(String id, String buyerId, HttpServletRequest request) {
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
			int i = consignAddressService.delete(id,buyerId);
			if(i>0){
				msg.setCode(0);
			}else{
				msg.setError("删除失败");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 获得默认地址(通用)
	 * @param buyerId
	 * @param request
	 * @return
	 */
	private ResponseMsg getDefault(Integer buyerId, HttpServletRequest request) {
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
			msg.setData(consignAddressService.getDefaultByBuyerId(buyerId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 查询零售商收货地址(通用)
	 * @param buyerId
	 * @param request
	 * @return
	 */
	private ResponseMsg getBuyerConsignAddress(Integer buyerId,
			HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		if(buyerId == null){
			LoginInfo loginInfo = MemberTools.isSaasLogin(request);
			if(null == loginInfo || null == loginInfo.getDealerId()){
				return msg.setError("请先登录");
			}else{
				buyerId = Integer.parseInt(loginInfo.getDealerId());
			}
		}
		try {
			List<BuyerConsignAddressPage> list = consignAddressService.findByBuyerId(buyerId);
            /**
             * create by yr on 2018-10-20
             * 增加行政区划
             */
            for(int i=0;i<list.size();i++){
                String provinceId = list.get(i).getProvinceId()!=null?list.get(i).getProvinceId().toString():"";
                String cityId = list.get(i).getProvinceId()!= null?list.get(i).getCityId().toString():"";
                String areaId = list.get(i).getProvinceId()!=null?list.get(i).getAreaId().toString():"";
                String areaName = orderGoodsDao.findNameById(areaId);
                String cityName = orderGoodsDao.findNameById(cityId);
                String provinceName = orderGoodsDao.findNameById(provinceId);
                list.get(i).setAreaName(areaName);
                list.get(i).setCityName(cityName);
                list.get(i).setProvinceName(provinceName);
            }
            msg.setData(list);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
			e.printStackTrace();
		}
		return msg;
	}
}
