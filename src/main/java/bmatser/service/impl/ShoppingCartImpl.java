package bmatser.service.impl;

import bmatser.dao.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.model.FindShoppingCartByIdsMall;
import bmatser.model.FindShoppingCartByType;
import bmatser.model.GoodsPackage;
import bmatser.model.OrderCountModel;
import bmatser.model.SellerGoods;
import bmatser.model.ShoppingCart;
import bmatser.pageModel.MakeOrderPage;
import bmatser.pageModel.ShoppingCartPage;
import bmatser.pageModel.util.MakeOrderGoodsPageUtil;
import bmatser.pageModel.util.MakeOrderPageUtil;
import bmatser.service.AmountAlgorithmI;
import bmatser.service.FreightAmountI;
import bmatser.service.GbeansServiceI;
import bmatser.service.GoodsActivityI;
import bmatser.service.ShoppingCartI;
import bmatser.util.Constants;
import bmatser.util.LogoException;
import bmatser.util.ResponseData;

/**
 * 购物车业务类
 * @author felx
 * 2017-9-16
 */
@Service("cartService")
public class ShoppingCartImpl extends ShoppingCartSupperImpl implements ShoppingCartI {
	

	
	//private final String[] keyArray = {"工电宝","团购","秒杀","套餐"};

    @Autowired
    private OrderGoodsMapper orderGoodsDao;

	/**
	 * 加入购物车
	 * @param shoppingCartPage
	 * @return
	 * 2017-9-16
	 *
	 */
	@Override
	public int addShoppingCart(ShoppingCartPage shoppingCartPage) {
		String activityPrice = shoppingCartPage.getActivityPrice();
		if(StringUtils.isNotBlank(activityPrice)){
			shoppingCartPage.setSalePrice(activityPrice);
		}
		shoppingCartPage.setCreateTime(new Date());
		Integer sellerId = shoppingCartPage.getSellerId();
		if(sellerId == null){
			shoppingCartPage.setSellerId(Constants.DEFAULT_SELLER_ID);
		}
		return cartDao.addShoppingCart(shoppingCartPage);
	}
	
	/**
	 * 获取指定零售商的购物车列表
	 * @param buyerId
	 * @return
	 * 2017-9-16
	 *
	 */
	@Override
	public List findShoppingCart(int buyerId) {
		List list = cartDao.findShoppingCart(buyerId);
		for (int i = 0,len = list.size(); i < len; i++) {
			Map map =(Map)list.get(i);
			String isRate = String.valueOf(map.get("isRate"));
			String isSale = String.valueOf(map.get("isSale"));
			String rate = String.valueOf(map.get("rate"));
			String price = String.valueOf(map.get("price"));
			if(StringUtils.equals("1", isRate) && !StringUtils.equals("1", isSale) && map.get("rate")!=null){
				map.put("rate", Double.parseDouble(price)*Double.parseDouble(rate));
			}else{
				map.put("rate", 0);
			}
			list.set(i, map);
		}
		return list;
	}
	@Override
	@Deprecated
	public MakeOrderPageUtil findShoppingCartByIds(int buyerId, String ids,String isCash,String addrId) throws Exception {
		MakeOrderPageUtil order = new MakeOrderPageUtil();
		DecimalFormat df   = new DecimalFormat("######0.00");
		List<String> idList = new ArrayList<String>();
		//切割字符串
		for(String id : ids.split(",")){
			idList.add(id.trim());
		}
		List<MakeOrderGoodsPageUtil> list = cartDao.findShoppingCartByIds(buyerId, idList);
		if(list==null || list.size() == 0){
			throw new Exception("购物车商品已不存在");
		}
		order.setIsCash(Integer.parseInt(isCash!=null?isCash:"1"));
		order.setDealerCash(dealerCashMapper.findCash(buyerId));
		//获取确认订单省Id
		String provinceId = "0";
		order.setBuyerAddr(buyerConsignAddressMapper.getDefaultByBuyerId(buyerId));
		if(order.getBuyerAddr()!=null){
			provinceId = order.getBuyerAddr().get("provinceId").toString();
		}
		if(StringUtils.isNotBlank(addrId)){
			Map<String, Object> map = buyerConsignAddressMapper.findById(Integer.parseInt(addrId));
			if(map!=null){
				provinceId =map.get("provinceId").toString();
			}
		}
		//运费
		order.setTotalFreight(df.format(amountAlgorithmI.getFreightAmount(idList, provinceId).doubleValue()));
		//商品
		order.setGoods(list);
		//满减金额
		Map<Long, String> query = order.getSellerGoodsIdArray();
		if(!query.isEmpty()){
			order.setTotalFullcut(df.format(activityService.findGoodsActivityFullcut(query)));
		}else{
			order.setTotalFullcut("0.00");
		}
		//默认地址
		//默认发票
		order.setBuyerInv(buyerInvoiceI.getDefaultByBuyerId(buyerId));
		order.setBean(gbeansServiceI.getCartsBeans(ids.split(",")));
		order.setUseBean(gbeansServiceI.getUseBeans(String.valueOf(buyerId)));
		
		return order.toCreateOrder();
	}
	
	@Override
	public OrderCountModel findSaasShoppingCart(int buyerId, String ids,String isCash,String addrId){
		if(isCash==null) isCash="1";
		String provinceId = "0";
		List<String> idList = new ArrayList<String>();
		//切割字符串
		for(String id : ids.split(",")){
			idList.add(id.trim());
		}
		OrderCountModel order = new OrderCountModel();
		order.setIsCash(Integer.parseInt(isCash));

		order.setDefaultList(cartDao.findCartBytype(buyerId, idList, 0));
		order.setGroupList(cartDao.findCartBytype(buyerId, idList, 1));
		List<GoodsPackage> packageList = cartDao.getPackageInfoByCartId(idList);
		if(packageList != null && packageList.size()>0){
			order.setPackageList(packageList);
			order.setPackageGoodsList(cartDao.findPackageShoppingCartByIds(buyerId, packageList));
		}
		//满减金额
//		Map<Long, String> query = order.getSellerGoodsIdArray();
//		if(!query.isEmpty()){
//			order.setTotalFullcut(new BigDecimal(String.valueOf(activityService.findGoodsActivityFullcut(query))));
//		}else{
//			order.setTotalFullcut(BigDecimal.ZERO);
//		}
		order.setBuyerAddr(buyerConsignAddressMapper.getDefaultByBuyerId(buyerId));
		if(order.getBuyerAddr()!=null){
			provinceId = order.getBuyerAddr().get("provinceId").toString();


            /**
             * create by yr on 2018-10-29
             * 增加行政区划
             */
            String  cityId = order.getBuyerAddr().get("cityId")!=null?order.getBuyerAddr().get("cityId").toString():"";
            String  areaId = order.getBuyerAddr().get("areaId")!=null?order.getBuyerAddr().get("areaId").toString():"";
            String provinceName =  orderGoodsDao.findNameById(provinceId);
            String cityName = orderGoodsDao.findNameById(cityId);
            String areaName = orderGoodsDao.findNameById(areaId);
            order.getBuyerAddr().put("provinceName",provinceName);
            order.getBuyerAddr().put("cityName",cityName);
            order.getBuyerAddr().put("areaName",areaName);

		}
		if(StringUtils.isNotBlank(addrId)){
			Map<String, Object> map = buyerConsignAddressMapper.findById(Integer.parseInt(addrId));
			if(map!=null){
				provinceId =map.get("provinceId").toString();
			}
		}
		order.setTotalFreight(amountAlgorithmI.getFreightAmount(idList, provinceId));
		order.setBuyerInv(buyerInvoiceI.getDefaultByBuyerId(buyerId));
		order.setBean(gbeansServiceI.getCartsBeans(ids.split(",")));
		order.setUseBean(gbeansServiceI.getUseBeans(String.valueOf(buyerId)));
		order.setDealerCash(dealerCashMapper.findCash(buyerId));
		return order;
	}

	
	
	
	
	/**
	 * 添加到购物车
	 * @param shoppingCart 购物车页面参数
	 * @throws Exception 
	 * 
	 */
/*	@Override
	public Integer addGoods(ShoppingCartPage shoppingCart) throws Exception {
		if(shoppingCart.getBuyerId()==null || "".equals(shoppingCart.getBuyerId())){
			throw new Exception("帐号不存在");
		}
		if(shoppingCart.getSellerGoodsId()==null || "".equals(shoppingCart.getSellerGoodsId())){
			throw new Exception("商品信息错误");
		}
		if(shoppingCart.getNum()==null || "".equals(shoppingCart.getNum())){
			shoppingCart.setNum(1);
		}
		SellerGoods goods = sellerGoodsMapper.findGoodsPrice(shoppingCart.getSellerGoodsId());
		ShoppingCart cart = cartDao.getShoppingCartByPageId(shoppingCart.getBuyerId(), shoppingCart.getSellerGoodsId());
		if(cart==null){
			cart = new ShoppingCart();
		}
		try {
			int num = cart.getNum()!=null?cart.getNum().intValue():0;
			cart.setBuyerId(shoppingCart.getBuyerId());
			if(shoppingCart.getuNum()==0){
				cart.setNum(Math.abs(num+shoppingCart.getNum().intValue()));
			}else{
				cart.setNum(shoppingCart.getuNum());
			}
			cart.setSellerId(goods.getSellerId());
			cart.setSellerGoodsId(shoppingCart.getSellerGoodsId());
			cart.setCreateTime(new Date());
			cart.setSalePrice(goods.getSalePrice());
			cart.setStatus("1");
			if(cart.getId()==null || "".equals(cart.getId())){
				cartDao.save(cart);
			}else{
				cartDao.update(cart);
			}
			return cart.getId();
		} catch (Exception e) {
			throw new LogoException(e,"商品添加失败,请联系客服");
		}
	}*/
	@Override
	public Integer addGoods(ShoppingCartPage shoppingCart) throws Exception{
		Integer buyerId = shoppingCart.getBuyerId();
		Integer id = shoppingCart.getId();
		if(buyerId == null) 
			throw new RuntimeException("帐号不存在");
		int num = shoppingCart.getNum();
		Integer uNum = shoppingCart.getuNum()==0?null:shoppingCart.getuNum();
		String sellerGoodsId = shoppingCart.getSellerGoodsId();
		String packageId= shoppingCart.getPackageId();
		if(StringUtils.isBlank(sellerGoodsId) && StringUtils.isBlank(packageId) && (id == null || id == 0)) 
			throw new RuntimeException("商品信息错误");
		if(id!=null && id != 0){//购物车Id
			cartDao.updateCartByType(id, buyerId.toString(),  num, uNum);
		}else if(StringUtils.isNotBlank(packageId)){//套餐Id
			addPackage(packageId,buyerId.toString(), num, uNum);
		}else{
			String[] ids = sellerGoodsId.split(",");
			for (String value : ids) {
				SellerGoods goods = sellerGoodsMapper.findGoodsPrice(value);
				String series = sellerGoodsMapper.getSeries(goods.getGoodsNo());
                BigDecimal rate1 = sellerGoodsMapper.getDiscountByGoodsNo(goods.getGoodsNo(),buyerId.toString());
                BigDecimal rate2 = sellerGoodsMapper.getDiscountBySeries(series,buyerId.toString());
                BigDecimal marketPrice = sellerGoodsMapper.getMarketprice(value);
                BigDecimal rate = null;
                if(rate1 != null){
                    rate = rate1;
                }
                else if(rate2 != null) {
                    rate = rate2;
                }
				if(goods == null) throw new RuntimeException("商品信息错误");
				int activityType = 0;
				String activityId = null;
				if(shoppingCart.getActivityType()==1){
					ShoppingCart cart = getGroupActivityCart(buyerId,value);
					activityType = cart.getActivityType();
					activityId = cart.getActivityId();
				}
				if(rate1!=null||rate2!=null){
                    goods.setSalePrice((marketPrice.multiply(rate)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    cartDao.addOrUpdateCartByDiscount(value, buyerId.toString(),goods.getSalePrice(),num, uNum, activityType, activityId);
                }else {
                    cartDao.addOrUpdateCart(value, buyerId.toString(),  num, uNum, activityType, activityId);
                }

			}
		}
		return null;
	}
	/**
	 * 普通加入购物车
	 * @param buyerId
	 * @param sellerGoodsId
	 * @return
	 */
	private ShoppingCart getShoppingCart(Integer buyerId, String sellerGoodsId) {
		return cartDao.getShoppingCartByPageId(buyerId, sellerGoodsId);
	}
	/**
	 * 
	 * @param buyerId
	 * @param sellerGoodsId
	 * @return
	 */
	private ShoppingCart getGroupActivityCart(Integer buyerId,
			String sellerGoodsId) {
		ShoppingCart cart = null;
		int isGroup = grouponActivityMapper.isGroupGoods(buyerId.toString(), sellerGoodsId);
		if(isGroup == 1){
			String  grouponActivityId = grouponActivityMapper.findGrouponActivityId(buyerId.toString(), sellerGoodsId);
			if(grouponActivityId == null){
				return cartDao.getShoppingCartByPageId(buyerId, sellerGoodsId);
			}
			cart = cartDao.getShoppingCartByPageGroupId(buyerId,sellerGoodsId,grouponActivityId);
			if(cart == null){
				cart = new ShoppingCart();
				cart.setActivityType(1);
				cart.setActivityId(grouponActivityId);
			}
			return cart;
		}else{
			return cartDao.getShoppingCartByPageId(buyerId, sellerGoodsId);
		}
	}

	/**
	 * 商城修改购物车
	 * @param shoppingCart 购物车页面参数
	 * 
	 */
	@Override
	public void editOrderCartMall(ShoppingCartPage shoppingCart) throws Exception {
//		if(shoppingCart.getBuyerId()==null || "".equals(shoppingCart.getBuyerId())){
//			throw new Exception("帐号不存在");
//		}
//		if(shoppingCart.getSellerGoodsId()==null || "".equals(shoppingCart.getSellerGoodsId())){
//			throw new Exception("商品信息错误");
//		}
//		ShoppingCart cart = cartDao.getShoppingCartById(shoppingCart.getBuyerId(), shoppingCart.getSellerGoodsId());
		ShoppingCart cart = cartDao.getCartById(shoppingCart.getId());
		if(cart==null){
			throw new Exception("商品信息错误");
		}else{
			String status = shoppingCart.getStatus();
			try {
				if(null!=status && "2".equals(status)){
					//删除购物车
					cartDao.delete(cart.getId());
				}else{
					if(null != shoppingCart.getNum()){
						if(shoppingCart.getNum()<0 && cart.getNum().equals(Math.abs(shoppingCart.getNum()))){
							//当订单中数量和购物车中数量一样即将该购物车信息改成无效
							//					cart.setStatus("2");
							cartDao.delete(cart.getId());
						}else{
							cart.setNum(Math.abs(cart.getNum() + shoppingCart.getNum()));
							cart.setCreateTime(new Date());
							cartDao.update(cart);
						}
					}
				}
			} catch (Exception e) {
				throw new LogoException(e,"购物车修改失败,请联系客服");
			}
		}
	}
	
	/**
	 * 获取商城购物车列表
	 * @param buyerId
	 * @return
	 * 2017-9-16
	 *
	 */
	@Override
	public List findShoppingCartMall(int buyerId) {
		List resultList = new ArrayList();
		List shopList = cartDao.findShoppingCartShop(buyerId,null);
		if(!shopList.isEmpty()){
			for(int i=0;i<shopList.size();i++){
				ShoppingCartPage cart = new ShoppingCartPage();
				Map map =(Map)shopList.get(i);
				Integer shopId = Integer.valueOf(map.get("id").toString());
				List list = cartDao.findShoppingCartMall(buyerId,shopId);
				cart.setShopName(map.get("shopName")==null?null:map.get("shopName").toString());
				cart.setAlias(map.get("alias")==null?null:map.get("alias").toString());
				cart.setSellerId(shopId);
				cart.setGoodsList(list);
				resultList.add(cart);
			}
		}
		return resultList;
	}
	
	
	/**
	 * 根据id集合获取商城购物车商品列表
	 * @author felx
	 * @date 2017-12-28
	 */
	public Map findShoppingCartByIdsMall(int buyerId, String ids,String addressId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<String> idList = new ArrayList<>();
		for(String id : ids.split(",")){
			idList.add(id.trim());
		}
		DecimalFormat myformat=new DecimalFormat("0.00");
		double totalGoodsPriceAll = 0;
		double totalCashAll = 0;
		double totalOrderPriceAll = 0;
		double totalFreightAll = 0;
		double totalFullcutAll = 0;
		/**获取订单运费*/
		int isjd = cartDao.isExistJdGoods(ids.split(","));
		if(isjd>0){
			resultMap.put("buyerAddr", buyerConsignAddressMapper.getDefaultJDByBuyerId(buyerId));
		}else{
			resultMap.put("buyerAddr", buyerConsignAddressMapper.getDefaultByBuyerId(buyerId));
		}
		if(resultMap.get("buyerAddr")!=null){
			Map<String, Object> map = (Map<String, Object>) resultMap.get("buyerAddr");
			String id = map.get("id").toString();
			if(StringUtils.isBlank(addressId)){
				addressId = id;
			}
		}
		Map freMap = freightAmountService.getFreightAmount(ids, addressId);
		totalFreightAll = Double.valueOf(freMap.get("freightAmount").toString());
		List shopList = cartDao.findShoppingCartShop(buyerId,ids.split(","));
		List<Map<String, Object>> sellerList = new ArrayList<>();
		if(!shopList.isEmpty()){
			for(int i=0;i<shopList.size();i++){
				double totalGoodsPrice = 0;
				double totalCash = 0;
				double totalOrderPrice = 0;
				double totalFreight = 0;
				Map<Integer, Object> priceMap = new HashMap<>();
				Map<String, Object> resposeMap = new HashMap<>();
				ShoppingCartPage cart = new ShoppingCartPage();
				Map shopMap =(Map)shopList.get(i);
				Integer shopId = Integer.valueOf(shopMap.get("id").toString());
				List<FindShoppingCartByIdsMall> list = cartDao.findShoppingCartByIdsMall(buyerId, idList, shopId);
				resposeMap.put("shopName",shopMap.get("shopName").toString());
				resposeMap.put("alias",shopMap.get("alias")==null?null:shopMap.get("alias").toString());
				resposeMap.put("sellerId",shopId);
				resposeMap.put("goods", list);
				
				Map<Long, Object> fullcutMap = new HashMap<>();
				for(FindShoppingCartByIdsMall o : list){
					if(o!=null && o.getNum() != 0){
						Long sellerGoodsId = o.getSellerGoodsId();
						Integer brandId = o.getBrandId();
						Double gPrice = o.getPayPrice().doubleValue();
						totalGoodsPrice += gPrice;
						totalOrderPrice += gPrice;
						totalGoodsPriceAll += gPrice;
						totalOrderPriceAll += gPrice;
						totalCash += o.countCash();
						totalCashAll += o.countCash();
						if(priceMap.containsKey(brandId)){
							priceMap.put(brandId, gPrice+Double.valueOf(priceMap.get(brandId).toString()));
						} else{
							priceMap.put(brandId, gPrice);
						}
						if(fullcutMap.containsKey(sellerGoodsId)){
							fullcutMap.put(sellerGoodsId, gPrice+Double.valueOf(fullcutMap.get(sellerGoodsId).toString()));
						} else{
							fullcutMap.put(sellerGoodsId, gPrice);
						}
					}

				}
				Double totalFullcut = activityService.findGoodsActivityFullcut(fullcutMap);
				totalFullcutAll += totalFullcut;
				
				BigDecimal bd = new BigDecimal(totalGoodsPrice);
				totalGoodsPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				resposeMap.put("totalGoodsPrice", totalGoodsPrice);
				resposeMap.put("totalCash", (int)totalCash);
				resposeMap.put("totalScore", (int)(totalGoodsPrice*(Constants.SCORE_RATE)));
				resposeMap.put("totalFreight", totalFreight);
				resposeMap.put("totalPayPrice", myformat.format(totalGoodsPrice + totalFreight - totalFullcut));
				resposeMap.put("totalOrderPrice", myformat.format(totalOrderPrice + totalFreight));
				resposeMap.put("totalFullcut", myformat.format(totalFullcut));
				sellerList.add(resposeMap);
			}
		}
		resultMap.put("orderTotalGoodsPrice", myformat.format(totalGoodsPriceAll));
		resultMap.put("orderTotalCash", (int)totalCashAll);
		resultMap.put("orderTotalScore", (int)(totalGoodsPriceAll*(Constants.SCORE_RATE)));
		resultMap.put("orderTotalFreight", totalFreightAll);
		resultMap.put("orderTotalPayPrice", myformat.format(totalGoodsPriceAll + totalFreightAll - 0));
		resultMap.put("orderTotalOrderPrice", myformat.format(totalOrderPriceAll + totalFreightAll));
		resultMap.put("orderTotalFullcut", 0);
		resultMap.put("isExistJD", isjd>0?1:0);
		
		resultMap.put("shops", sellerList);

		
		resultMap.put("buyerInv", buyerInvoiceI.getDefaultByBuyerId(buyerId));
		
		return resultMap;
	}
	/**
	 * 批量更新购物车
	 */
	@Override
	public void batchaAddCart(String cartIds, String nums) throws Exception {
		if(StringUtils.isBlank(cartIds) || StringUtils.isBlank(nums)){
			throw new Exception("参数未发现");
		}
		String[] cartIdsArr = cartIds.split(",");
		String[] numsArr = nums.split(",");
		if(cartIdsArr.length == numsArr.length){
			for (int i = 0; i < cartIdsArr.length; i++) {
				ShoppingCart cart = cartDao.findShoppingCartById(cartIdsArr[i]);
				if(cart!=null&&StringUtils.isNotBlank(numsArr[i])){
					cart.setNum(Integer.parseInt(numsArr[i]));
					cart.setCreateTime(new Date());
					cartDao.update(cart);
				}else{
					throw new Exception("参数不正确");
				}
			}
		}else{
			throw new Exception("参数不对应");
		}
	}
	
	/**
	 * 获取会员购物车商品总数
	 * @author felx
	 * @date 2016-1-7
	 */
	public Integer getCartNumById(Integer buyerId) throws Exception {
		return cartDao.getCartNumById(buyerId);
	}
	
	/**
	 * 删除购物车商品
	 * @author felx
	 * @date 2016-1-7
	 */
	public void delCart(Integer id) throws Exception {
		cartDao.delete(id);
	}

	@Override
	public void batchDelcart(String[] cartIds) {
		cartDao.batchDelcart(cartIds);
		
	}

	@Override
	public void delCart(String cartId) {
		cartDao.batchDelcart(cartId.split(","));
	}
	@Override
	public void deleteCart(String cartId,String dealerId) {
		String[] str = cartId.split(",");
		for (String id : str) {
			cartDao.deleteCart(id,dealerId);
		}
	}

	@Override
	public double getCash(Integer buyerId,String cartId) {
		List idList = new ArrayList();
		for(String id : cartId.split(",")){
			idList.add(id.trim());
		}
		List list = cartDao.findShoppingCartByIds(buyerId, idList);
		double totalCash = 0;
		for(Object o : list){
			Map map = (Map)o;
			String price = String.valueOf(map.get("price"));
			String rate = String.valueOf(map.get("rate"));
			String isRate = String.valueOf(map.get("isRate"));
			String num = String.valueOf(map.get("num")==null?0 : map.get("num"));
			Double gPrice = 0D;
			if(StringUtils.equals("1", isRate)){
				gPrice = Double.valueOf(price)*Integer.parseInt(num);
				int goodsCash = (int) (gPrice*Double.parseDouble(rate));
				totalCash += goodsCash;
			}
		}
		return Math.floor(totalCash);
	}

	@Override
	public List<Map<String, Object>>  findShoppingCartList(Integer buyerId) {
		//private final String[] keyArray = {"工电宝","团购","秒杀","套餐"};
		//ResponseData data = new ResponseData(keyArray);
		/*for (int i = 0,len=keyArray.length; i < len; i++) {
			List<FindShoppingCartByType> list =cartDao.findShoppingCartByType(buyerId,i);
			data.addResponseList(list);
		}*/
		List<FindShoppingCartByType> defaultList =cartDao.findShoppingCartByType(buyerId,0);
		if(defaultList!=null && defaultList.size()>0){
			for(FindShoppingCartByType normal : defaultList){
				normal.setSelected(true);
				if(! normal.getCartPrice().toString().equals(normal.getSalePrice())){
                    normal.setSalePrice(normal.getCartPrice().toString());
                }
			}
		}
		List<FindShoppingCartByType> groupList = cartDao.findShoppingCartByType(buyerId,1);
		if(groupList!=null && groupList.size()>0){
			for(FindShoppingCartByType group : groupList){
				group.setSelected(true);
				if(! group.getCartPrice().toString().equals(group.getSalePrice())){
                    group.setSalePrice(group.getCartPrice().toString());
                }
			}
		}
		
		List<GoodsPackage> packageList = getPackageShoppingCart(buyerId);
		List<Map<String, Object>> list = new ArrayList<>();
		if(defaultList!=null && defaultList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "工电宝");
			map.put("list", defaultList);
			map.put("type", 0);
			list.add(map);
		}
		if(groupList!=null && groupList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "团购");
			map.put("list", groupList);
			map.put("type", 1);
			list.add(map);
		}
		if(packageList!=null && packageList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "套餐");
			map.put("list", packageList);
			map.put("type", 3);
			list.add(map);
		}


		return list;
	}

    /**
     * create by yr on 2018-09-17
     */
    public static BigDecimal getNewDiscount(FindShoppingCartByType all){


	    return null;
    }
	/**
	 * @author felx
	 * @describe 获取结构套餐
	 * @param buyerId 零售商Id
	 * @return
	 */
	private List<GoodsPackage> getPackageShoppingCart(Integer buyerId) {
		List<FindShoppingCartByType> packageList = cartDao.findShoppingCartByType(buyerId,3);
		if(packageList==null || packageList.size()==0) 
			return null;
		List<GoodsPackage> list = cartDao.getShoppingCartPackageInfo(buyerId);
		for (FindShoppingCartByType cart : packageList) {
			for (GoodsPackage p : list) {
				String packageId = p.getPackageId();
				if(packageId.equals(cart.getActivityId())){
					if(p.getGoodsList()!=null){
						p.getGoodsList().add(cart);
					}else{
						p.setGoodsList(new ArrayList<Object>());
						p.getGoodsList().add(cart);
					}
				}
			}
		}
		return list;
	}

	/**
	 * @author felx
	 * @describe  套餐加入购物车
	 * @param packageId 套餐Id 
	 * @param dealerId 零售商Id
	 * @param num 加入数量
	 */
	@Override
	public synchronized void addPackage(String packageId, String dealerId, int num) {
		if(num == 0) return;
		Map<String, Object> info = goodsPackageDao.getPackageInfo(packageId);
		if(info == null) throw new RuntimeException("套餐不存在");
		if("3".equals(info.get("combinationType").toString())){
			String ids = info.get("ids").toString();
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				cartDao.addOrUpdateCart(id,dealerId,num,null,0,null);
			}
		}else{
			cartDao.addOrUpdatePackageCart(packageId,dealerId,num,null);
		}
	}
	/**
	 * @author felx
	 * @describe  套餐加入购物车
	 * @param packageId 套餐Id 
	 * @param dealerId 零售商Id
	 * @param num 加入数量
	 * @throws Exception 
	 */
	public synchronized void addPackage(String packageId, String dealerId, int num,Integer uNum) throws Exception {
		if(num == 0) return;
		Map<String, Object> info = goodsPackageDao.getPackageInfo(packageId);
		if(info == null) throw new Exception("套餐不存在");
		if("3".equals(info.get("combinationType").toString())){
			String ids = info.get("ids").toString();
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				cartDao.addOrUpdateCart(id,dealerId,num,null,0,null);
			}
		}else{
			cartDao.addOrUpdatePackageCart(packageId,dealerId,num,uNum);
		}
	}

	@Override
	public List<Map<String, Object>> findAppShoppingCartList(Integer dealerId) {
		List<FindShoppingCartByType> defaultList =cartDao.findShoppingCartByType(dealerId,0);
		List<FindShoppingCartByType> groupList = cartDao.findShoppingCartByType(dealerId,1);
		List<GoodsPackage> packageList = getPackageShoppingCart(dealerId);
		List<Map<String, Object>> list = new ArrayList<>();
		List<Object> appList = new ArrayList<Object>();
		if(defaultList!=null){
			appList.addAll(defaultList);
		}
		if(packageList!=null){
			appList.addAll(packageList);
		}
		if(appList!=null && appList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "工电宝");
			map.put("list", appList);
			map.put("type", 0);
			list.add(map);
		}
		if(groupList!=null && groupList.size()>0){
			Map<String, Object> map = new HashMap<>();
			map.put("name", "团购");
			map.put("list", groupList);
			map.put("type", 1);
			list.add(map);
		}

		return list;
	}


}
