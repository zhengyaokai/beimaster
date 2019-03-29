/**
 * 店铺(XX旗舰店)
 */
package bmatser.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.rubyeye.xmemcached.MemcachedClient;

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

import bmatser.model.Category;
import bmatser.model.SellerGoods;
import bmatser.model.StoreSearchParam;
import bmatser.service.GoodsStoreI;
import bmatser.util.ResponseMsg;

/**
 * @author felx
 * 
 */
@Controller
@RequestMapping("store")
@Api(value="store", description="店铺")
public class GoodsStoreController {

	@Autowired
	private GoodsStoreI goodsStoreI;
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	/**
	 * 通过别名进入店铺
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/{alias}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "通过别名进入店铺")
	public ResponseMsg findStoreInfo(@ApiParam(value="店铺别名") @PathVariable("alias")String alias){
		ResponseMsg msg = new ResponseMsg();
		try {
			Map map = goodsStoreI.findStoreInfo(alias.toLowerCase());
			msg.setData(map);
			msg.setCode(0);
			if(null != map && null != map.get("shopName")){
				memcachedClient.set("shopTitle"+alias, 0,map.get("shopName"));//商品标题方法缓存
			}
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("加载失败:" + e.getMessage());
		}
		return msg;
	}
	/**
	 * 通过别名获得店铺分类
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/{alias}/cate",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "通过别名获得店铺分类", response = Category.class)
	public ResponseMsg findStoreCate(@ApiParam(value="店铺别名")@PathVariable("alias")String alias){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(goodsStoreI.findStoreCate(alias));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("加载失败:" + e.getMessage());
		}
		return msg;
	}
	/**
	 * 通过别名获得店铺子分类
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/{alias}/cate/{cateId}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "通过别名获得店铺子分类", response = Category.class)
	public ResponseMsg findStoreCate(@ApiParam(value="店铺别名") @PathVariable("alias")String alias,@ApiParam(value="分类ID") @PathVariable("cateId")String cateId){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(goodsStoreI.findStoreCate(alias,cateId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("加载失败:" + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 店铺宝贝排行榜
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/{alias}/top",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "店铺宝贝排行榜")
	public ResponseMsg findStoreTop(@ApiParam(value="店铺别名") @PathVariable("alias")String alias){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(goodsStoreI.findStoreTop(alias));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("加载失败:" + e.getMessage());
		}
		return msg;
	}
	/**
	 * 店铺分类宝贝
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/{alias}/goods",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "店铺分类宝贝", response = SellerGoods.class)
	public ResponseMsg findStoreCateGoods(@ApiParam(value="店铺别名") @PathVariable("alias")String alias,
			@ApiParam(value="分类") @RequestParam(required=false) String cateNo,
	        @ApiParam(value="页数") @RequestParam(required=false) String page,
	        @ApiParam(value="行数") @RequestParam(required=false) String row,
	        HttpServletRequest req){
		ResponseMsg msg = new ResponseMsg();
		/*String cateNo = req.getParameter("cateNo");
		String page = req.getParameter("page");
		String row = req.getParameter("row");*/
		try {
			if(StringUtils.isNotBlank(cateNo)){
				msg.setData(goodsStoreI.findStoreCateGoods(alias,cateNo,page,row));
			}else{
				msg.setData(goodsStoreI.findStoreCateGoods(alias));
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 店铺特卖宝贝
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/{alias}/act",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "店铺特卖宝贝", response = SellerGoods.class)
	public ResponseMsg findStoreGoodsActivity(@ApiParam(value="店铺别名") @PathVariable("alias")String alias){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(goodsStoreI.findStoreGoodsActivity(alias));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("加载失败:" + e.getMessage());
		}
		return msg;
	}
	/**
	 * 店铺页面搜索
	 * @param alias
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/{alias}/search",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "店铺页面搜索")
	public ResponseMsg findStoreSearch(@ModelAttribute StoreSearchParam storeSearchParam ,@ApiParam(value="店铺别名") @PathVariable("alias")String alias,HttpServletRequest req) throws UnsupportedEncodingException{
		ResponseMsg msg = new ResponseMsg();
		Map<String, String> map  = new HashMap<String, String>();
		/*if(req.getParameter("goodsName")!=null){
			map.put("goodsName", URLDecoder.decode(req.getParameter("goodsName"),"UTF-8"));
		}
		map.put("lowPrice", req.getParameter("lowPrice"));
		map.put("hightPrice", req.getParameter("hightPrice"));
		map.put("page", req.getParameter("page"));*/
		if(storeSearchParam.getGoodsName()!=null){
			map.put("goodsName", URLDecoder.decode(storeSearchParam.getGoodsName(),"UTF-8"));
		}
		map.put("lowPrice", storeSearchParam.getLowPrice());
		map.put("hightPrice", storeSearchParam.getHightPrice());
		map.put("page", storeSearchParam.getPage());
		try {
			msg.setData(goodsStoreI.findStoreSearch(alias,map));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("加载失败:" + e.getMessage());
		}
		return msg;
	}
	/**
	 * 通过别名获得店铺使用的模板及模板数据 20160808 tiw
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/{alias}/selectTemplatesAndData",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "通过别名获得店铺使用的模板及模板数据")
	public ResponseMsg selectTemplatesAndData(@ApiParam(value="店铺别名") @PathVariable("alias")String alias){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(goodsStoreI.selectTemplatesAndData(alias));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 根据活动id查询出活动商品 20160808 tiw
	 * @return
	 */
	@RequestMapping(value="/selectActivityGoods/{page}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据活动id查询出活动商品", response = SellerGoods.class)
	public ResponseMsg selectActivityGoods(HttpServletRequest request,@ApiParam(value="页数")@PathVariable int page,
			@ApiParam(value="活动id") @RequestParam String id){
		int rows = 40;
		ResponseMsg msg = new ResponseMsg();
		/*String id=request.getParameter("id");*/
		if(StringUtils.isBlank(id)){
			throw new RuntimeException("参数错误!");
		}
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		try {
			msg.setData(goodsStoreI.findActivityGoods(id,page*rows,rows));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
}
