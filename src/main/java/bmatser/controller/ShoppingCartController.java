package bmatser.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import bmatser.annotations.Login;
import bmatser.annotations.ResponseDefault;
import bmatser.model.LoginInfoUtil;
import bmatser.model.Version;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.ShoppingCartPage;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.RegisterI;
import bmatser.service.ShoppingCartI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("cart")
@Api(value="cart", description="购物车")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartI cartService;
	@Autowired
	private RegisterI registerService;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "添加购物车", 
	notes = "添加购物车")
	public int addShoppingCart(ShoppingCartPage shoppingCartPage){
		return cartService.addShoppingCart(shoppingCartPage);
	}
	
	@RequestMapping(value="/todo",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "添加购物车", 
	notes = "添加购物车")
	@ResponseDefault
	@Login(app="/todo")
	public Object to(String ids,LoginInfoUtil loginInfo) throws NumberFormatException, Exception{
		return cartService.findSaasShoppingCart(Integer.parseInt(loginInfo.getDealerId()), ids, "1", null);
	}
	
	/**
	 * 删除购物车
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/{cartId}", method=RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "发货短信提醒", 
	notes = "发货短信提醒")
	@Deprecated
	public ResponseMsg deleteShoppingCart(@PathVariable String cartId,HttpServletRequest request,HttpServletResponse response){
		ResponseMsg rsp = new ResponseMsg();
		try {
			cartService.delCart(cartId);
			rsp.setCode(0);
		} catch (Exception e) {
			rsp.setError(e);
		}
		return rsp;
	}
	/**
	 * 删除购物车
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/mobile/del",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除购物车", 
	notes = "删除购物车")
	@ApiParam(required=false,name="cartId",value="购物车Id") 
	@Login(app="/mobile/del")
	@ResponseDefault
	public ResponseMsg deleteShoppingCartByMob(@ApiParam(value="购物车id")String cartId,LoginInfoUtil infoUtil){
/*		ResponseMsg rsp = new ResponseMsg();
		String cartId = req.getParameter("cartId");
		try {
			cartService.delCart(cartId);
			rsp.setCode(0); 
		} catch (Exception e) {
			rsp.setError(e);
		}*/
		cartService.deleteCart(cartId,infoUtil.getDealerId());
		return null;
	}
	/**
	 * @author felx
	 * @describe 删除购物车
	 * @param cartId 购物车id
	 * @return
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除购物车", notes = "删除购物车")
	@Login(saas="/del")
	@ResponseDefault
	public ResponseMsg deleteShoppingCartBySaas(
			@ApiParam(value="购物车id")String cartId,LoginInfoUtil infoUtil){
		cartService.deleteCart(cartId,infoUtil.getDealerId());
		return null;
	}
	/**
	 * 购物车列表
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/buyer/{buyerId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "购物车列表", 
	notes = "购物车列表")
	public ResponseMsg findShoppingCart(@PathVariable int buyerId){
		ResponseMsg rsp = new ResponseMsg();
		try {
			rsp.setData(cartService.findShoppingCart(buyerId)); 
			rsp.setCode(0);
		} catch (Exception e) {
			rsp.setError(e);
		}
		return rsp;
	}
	/**
	 * 购物车列表
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/buyer", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "购物车列表", 
	notes = "购物车列表")
	@Login(saas="/buyer")
	@ResponseDefault
	public Object findShoppingCartBySaas(LoginInfoUtil loginInfo){
		Integer dealerId = Integer.parseInt(loginInfo.getDealerId());
		return cartService.findShoppingCart(dealerId);
	}
	/**
	 * 购物车列表(区分团购)
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "购物车列表(区分团购)",
	notes = "购物车列表(区分团购)")
	@Login(saas="/list")
	@ResponseDefault
	public Object findShoppingCartListBySaas(LoginInfoUtil loginInfo){
		Integer dealerId = Integer.parseInt(loginInfo.getDealerId());
		return cartService.findShoppingCartList(dealerId);
	}
	
	/**
	 * 购物车数量
	 * @param buyerId
	 * @return
	 */
	@RequestMapping(value="/buyer/count", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "购物车数量", 
	notes = "购物车数量")
	public ResponseMsg findShoppingCartBySaasCount(HttpServletRequest request){
		ResponseMsg rsp = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return rsp.setError("请先登录");
		}
		try {
			List list = cartService.findShoppingCart(Integer.parseInt(loginInfo.getDealerId()));
			if(list!=null){
				rsp.setData(list.size());
			}else{
				rsp.setData(0);
			}
			rsp.setCode(0);
		} catch (Exception e) {
			rsp.setError(e);
		}
		return rsp;
	}
	/**
	 * 确认订单列表
	 * @param buyerId
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/ids/{buyerId}/{ids}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "确认订单列表", 
	notes = "确认订单列表")
	@ApiParam(required=false,name="isCash",value="是否现金券") 
	public ResponseMsg findShoppingCartByIds(@PathVariable int buyerId, @PathVariable String ids,HttpServletRequest request,HttpServletResponse response,
			@ApiParam(value="地址Id") @RequestParam(required=false) String addrId){
		ResponseMsg res = new ResponseMsg();
		String isCash = request.getParameter("isCash");
		try {
			res.setData(cartService.findShoppingCartByIds(buyerId, ids,isCash,addrId));
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		} 
		return res;
	}
	/**
	 * 确认订单列表
	 * @param buyerId
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/ids", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "确认订单列表", 
	notes = "确认订单列表")
	@ApiParam(required=false,name="ids",value="购物车Id") 
	public ResponseMsg findShoppingCartByIds(HttpServletRequest request,HttpServletResponse response,
		@ApiParam(required=false,name="ids",value="购物车Id") String ids,
		@ApiParam(required=false,name="isCash",value="是否现金券") String isCash,
		@ApiParam(value="地址Id") @RequestParam(required=false) String addrId){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return res.setError("请先登录");
		}
		try {
			res.setData(cartService.findShoppingCartByIds(Integer.parseInt(loginInfo.getDealerId()), ids,isCash,addrId));
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		} 
		return res;
	}

	/**
	 * 加入购物车
	 * @param shoppingCart
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/cart_add", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "加入购物车", 
	notes = "加入购物车")
	@ResponseDefault
	@Login(saas="/cart_add",app="/cart_add")
	public Object  addShoppingCartBySaas(ShoppingCartPage shoppingCart,HttpServletRequest request,LoginInfoUtil loginInfoUtil) throws Exception{
		Integer dealerId= Integer.parseInt(loginInfoUtil.getDealerId());
		shoppingCart.setBuyerId(dealerId);
		cartService.addGoods(shoppingCart);
		return registerService.loadUserCart(String.valueOf(shoppingCart.getBuyerId()));
	}


	/**
	 * 加入购物车
	 * @param shoppingCart
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/batch_add", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "快速下单批量加入购物车", 
	notes = "快速下单批量加入购物车")
	@ResponseDefault
	@Login(saas="/batch_add")
	public Object  addShoppingCartByQuick(
			@RequestBody ShoppingCartPage shoppingCart,
			HttpServletRequest request,
			LoginInfoUtil loginInfoUtil) throws Exception{
		Integer dealerId= Integer.parseInt(loginInfoUtil.getDealerId());
		shoppingCart.setBuyerId(dealerId);
		String[] sellingGoods = shoppingCart.getSellingGoodIds();
		Integer[] addNums = shoppingCart.getAddNums();
		if(sellingGoods!=null && sellingGoods.length>0){
			for(int i=0;i<sellingGoods.length;i++){
				shoppingCart.setSellerGoodsId(sellingGoods[i]);
				shoppingCart.setNum(addNums[i]);
				cartService.addGoods(shoppingCart);
			}
		}
		
		return registerService.loadUserCart(String.valueOf(shoppingCart.getBuyerId()));
	}
	
	
	
	/**
	 * 商城添加购物车
	 * @author felx
	 */
	@RequestMapping(value={"/mallAdd","/app/mallAdd"}, method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "商城添加购物车", 
	notes = "商城添加购物车")
	public ResponseMsg addShoppingCartByIds( ShoppingCartPage shoppingCart,HttpServletRequest request,HttpServletResponse response){
		ResponseMsg res = new ResponseMsg();
		PageMode model =new PageMode(request);
		StringBuffer buffer = request.getRequestURL();
		try {
			if(buffer.indexOf("/app/")>=0){
				shoppingCart.setBuyerId(Integer.valueOf(model.getDealerId(Channel.APP)));
			}else{
				shoppingCart.setBuyerId(Integer.valueOf(model.getDealerId(Channel.MALL)));
			}
			res.setData(cartService.addGoods(shoppingCart));
			res.setCode(0);
		} catch (Exception e) {
			res.setCode(-1);
			res.setMsg(e.getMessage());
			e.printStackTrace();
		} 
		
		return res;
	}

	
	/**
	 * 商城查看购物车
	 * @author felx
	 */
	@RequestMapping(value="/cartList", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城查看购物车", 
	notes = "商城查看购物车")
	public ResponseMsg findShoppingCartMall(HttpServletRequest request){
		ResponseMsg rsp = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			Integer dealerId = model.getDealerIdDecimalVal(Channel.MALL).intValue();
			rsp.setData(cartService.findShoppingCartMall(dealerId));
		} catch (Exception e) {
			rsp.setError(e);
		}
		return rsp;
	}
	
	/**
	 * APP商城查看购物车
	 */
	@RequestMapping(value="/appMallCartList", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "APP商城查看购物车", 
	notes = "APP商城查看购物车")
	public ResponseMsg findShoppingCartAppMall(HttpServletRequest request,String dealerId){
		ResponseMsg rsp = new ResponseMsg();
		if(StringUtils.isBlank(dealerId)){
			rsp.setMsg("请先登录");
			rsp.setCode(-1);
			return rsp;
		}
		try {
			rsp.setData(cartService.findShoppingCartMall(Integer.valueOf(dealerId)));
		} catch (Exception e) {
			rsp.setError(e);
		}
		return rsp;
	}
	
	/**
	 * 商城确认购物车列表
	 * @author felx
	 */
	@RequestMapping(value="/cartOrder", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城确认购物车列表", 
	notes = "商城确认购物车列表")
	public ResponseMsg findShoppingCartByIdsMall(HttpServletRequest request,String ids,String addressId){
		ResponseMsg msg = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			Integer dealerId = model.getDealerIdDecimalVal(Channel.MALL).intValue();
			msg.setData(cartService.findShoppingCartByIdsMall(dealerId, ids, addressId));
		} catch (Exception e) {
			msg.setError(e);
		} 
		
		return msg;
	}
	
	/**
	 * APP商城确认购物车列表
	 */
	  @RequestMapping(value={"/app/cartOrder"}, method={RequestMethod.GET})
	  @ResponseBody
	  @ApiOperation(value="APP商城确认购物车列表", notes="APP商城确认购物车列表")
	  @Login(app="/app/cartOrder")
	  @ResponseDefault
	  public Object findAppShoppingCartByIdsMall(HttpServletRequest request, @ApiParam("购物车Id") String ids, @ApiParam("地址Id") String addressId, @ApiParam("零售商Id") String dealerId)
	    throws Exception
	  {
		ids = URLDecoder.decode(ids, "UTF-8");
	    return cartService.findShoppingCartByIdsMall(Integer.valueOf(dealerId).intValue(), ids, addressId);
	  }
	
	/**
	 * 修改购物车信息
	 * @author felx
	 */
	@RequestMapping(value="/editCart", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改购物车信息", 
	notes = "修改购物车信息")
	public ResponseMsg findShoppingCartByIdsMall(HttpServletRequest request,@ModelAttribute ShoppingCartPage shoppingCart,HttpServletResponse response){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			cartService.editOrderCartMall(shoppingCart);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		} 
		
		return res;
	}
	/**
	 * 批量更新购物车
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/batchAdd",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "批量更新购物车", 
	notes = "批量更新购物车")
	public ResponseMsg batchaAddCart(HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		String cartIds = request.getParameter("cartIds");
		String nums =  request.getParameter("num");
		try {
			cartService.batchaAddCart(cartIds,nums);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		} 
		return res;
		
	}
	/**
	 * 批量更新购物车
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/mobbatchAdd",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "批量更新购物车", 
	notes = "批量更新购物车")
	@Login(app="/mobbatchAdd")
	@ResponseDefault
	public ResponseMsg mobbatchaAddCart(String cartIds,String num,LoginInfoUtil loginInfo) throws Exception{
		//TODO
		Integer buyerId = Integer.parseInt(loginInfo.getDealerId());
		if(cartIds !=null && num != null){
			String[] cartIdArry = cartIds.split(",");
			String[] numArry =  num.split(",");
			for (int i = 0; i < cartIdArry.length; i++) {
				ShoppingCartPage shoppingCart = new ShoppingCartPage();
				shoppingCart.setId(Integer.parseInt(cartIdArry[i]));
				shoppingCart.setuNum(Integer.parseInt(numArry[i]));
				shoppingCart.setBuyerId(buyerId);
				cartService.addGoods(shoppingCart);
			}
		}
		return null;
	}
	/**
	 * 获取会员购物车商品数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{channel}/cartNum",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取会员购物车商品数", 
	notes = "获取会员购物车商品数")
	public ResponseMsg getCartNum(HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return res.setError("请先登录");
		}
		try {
			res.setData(registerService.loadUserCart(loginInfo.getDealerId()));
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}	
		return res;
		
	}
	/**
	 * 获取会员购物车商品数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cartNum",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取会员购物车商品数", 
	notes = "获取会员购物车商品数")
	public ResponseMsg cartNum(HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
		PageMode page = new PageMode(request);
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return res.setError("请先登录");
		}
		try {
			res.setData(cartService.getCartNumById(Integer.valueOf(page.getMallLogin().getDealerId())));
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}	
		return res;
		
	}
	
	/**
	 * 获取会员购物车商品数
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cartNumSaas",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取会员购物车商品数", 
	notes = "获取会员购物车商品数")
	public ResponseMsg cartNumSaas(HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return res.setError("请先登录");
		}
		try {
			res.setData(cartService.getCartNumById(Integer.valueOf(loginInfo.getDealerId())));
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}	
		return res;
		
	}
	
	/**
	 * 删除购物车信息
	 * @author felx
	 */
	@RequestMapping(value="/delCart", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除购物车信息", 
	notes = "删除购物车信息")
	public ResponseMsg delCartMall(HttpServletRequest request,String ids){
		ResponseMsg res = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			res.setMsg("请先登录");
			res.setCode(-1);
			return res;
		}
		try {
			List idList = new ArrayList();
			if(null!=ids && !"".equals(ids)){
				for(String id : ids.split(",")){
					if(null!=id && !"".equals(id)){
						cartService.delCart(Integer.valueOf(id.trim()));
					}
				}
			}
			res.setCode(0);
		} catch (Exception e) {
			res.setCode(-1);
			res.setMsg(e.toString());
		} 
		return res;
	}
	/**
	 * 总现金券
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cash",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "总现金券", 
	notes = "总现金券")
	public ResponseMsg getCash(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		String cartId = request.getParameter("cartId");
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return msg.setError("请先登录");
		}
		try {
			if(StringUtils.isNotBlank(cartId)){
				msg.setData(cartService.getCash(Integer.parseInt(loginInfo.getDealerId()),cartId));
			}else{
				msg.setError("参数错误");
			}
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
		
	}
	
	/**
	 * @author felx
	 * @describe 购物车列表
	 * @param loginInfo
	 * @return
	 */
	@RequestMapping(value={"/app/list","/saas/list"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "购物车列表", 
	notes = "购物车列表")
	@Login(saas="/saas/list",app="/app/list")
	@ResponseDefault
	public Object getGoodsDetail(LoginInfoUtil loginInfo){
		ResponseMsg msg = new ResponseMsg();
		Integer dealerId = Integer.parseInt(loginInfo.getDealerId());
		if(Channel.APP.equals(loginInfo.getChannel())){
			msg.setData(cartService.findAppShoppingCartList(dealerId));
			msg.setMsg(registerService.loadUserCart(dealerId.toString()));
		}else{
			msg.setData(cartService.findShoppingCartList(dealerId));
		}
		return msg;
	}
	/**
	 * @author felx
	 * @describe  套餐加入购物车
	 * @param packageId 套餐Id
	 * @param num 数量
	 * @param loginInfo
	 * @return
	 */
	@RequestMapping(value={"/saas/addPackage","/app/addPackage"},method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "购物车添加套餐", 
	notes = "购物车添加套餐")
	@Login(saas="/saas/addPackage",app="/app/addPackage")
	@ResponseDefault
	public Object addPackage(
			@ApiParam(value="套餐Id")String packageId,
			@ApiParam(value="加入数量")int num,
			LoginInfoUtil loginInfo
			){
		String dealerId = loginInfo.getDealerId();
		cartService.addPackage(packageId,dealerId,num);
		return null;
	}
	
	
	
}
