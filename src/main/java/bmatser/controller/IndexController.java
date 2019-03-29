package bmatser.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import bmatser.util.alibaba.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.model.Category;
import bmatser.pageModel.BuyerConsignAddressPage;
import bmatser.service.CategoryI;
import bmatser.service.IndexI;
import bmatser.util.ResponseMsg;

/**
 * 首页显示
 * @author felx
 */

@Controller
@RequestMapping("index")
@Api(value="index", description="首页")
public class IndexController {
	
	@Autowired
	private IndexI indexService;
	@Autowired
	private CategoryI categoryIService;
	/**
	 * 获取所有分类
	 * @author felx
	 * @date 2017-12-17
	 */
	@RequestMapping(value="/categories", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城获取所有分类", response = Category.class)
	public ResponseMsg categories(){
		ResponseMsg msg = new ResponseMsg();
		try {
			List<Category> categories = indexService.findCategories();//获取商城首页所有分类
			
			msg.setData(categories);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 获取所有楼层
	 * @author felx
	 * @date 2017-12-17
	 */
	@RequestMapping(value="/floors", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取商城首页所有楼层")
	public ResponseMsg floors(){
		ResponseMsg msg = new ResponseMsg();
		try {
			
			List floors = indexService.findFloors();//获取商城首页所有楼层
			
			msg.setData(floors);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 获取楼层分类
	 * @author felx
	 * @date 2017-12-17
	 */
	@RequestMapping(value="/floorCategory", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取楼层分类", response = Category.class)
	public ResponseMsg floorCategory(){
		ResponseMsg msg = new ResponseMsg();
		try {
			/*List adverImages = imageService.findAdverImages(4);//获取商城首页banner
			List<Map> goodsActivitys = activityService.findGoodsActivity(2);//获取商城特卖活动
			int count = 0;
			for(Map map : goodsActivitys){
				if(count == 0){
					Integer goodsActivityId = Integer.valueOf(map.get("id").toString());
					Map GoodsActivitySales = activityService.findGoodsActivitySales(goodsActivityId, null, null, null, 0, 10);
					System.out.println();
				}
				count++;
			}*/
			
			List floorCategorys = indexService.findFloorCategorys();//获取商城首页楼层分类
			
			msg.setData(floorCategorys);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 楼层分类
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="/{channel}/floor",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "楼层分类")
	public ResponseMsg getFloorCate(
			@ApiParam(value="渠道 mall") @PathVariable("channel")String channel){
		ResponseMsg msg = new ResponseMsg();
		try {
			Object data = null;
			switch (channel) {
			case "mall":
				data = categoryIService.getMallFloorCate();
				break;
			}
			msg.setData(data);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 获取楼层商品
	 * @author felx
	 * @date 2017-12-17
	 */
	@RequestMapping(value="/floorGoods", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取商城首页楼层商品")
	public ResponseMsg floorGoods(){
		ResponseMsg msg = new ResponseMsg();
		try {
			List floorGoods = indexService.findFloorGoods();//获取商城首页楼层商品
			
			msg.setData(floorGoods);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 获取楼层品牌
	 * @author felx
	 * @date 2017-12-17
	 */
	@RequestMapping(value="/floorBrands", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取商城首页楼层品牌")
	public ResponseMsg floorBrands(){
		ResponseMsg msg = new ResponseMsg();
		try {
			List floorBrands = indexService.findFloorBrands();//获取商城首页楼层品牌
			
			msg.setData(floorBrands);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城首页热门搜索   add--20160603
	 * @author felx
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="/{channel}/hotSearch",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城首页热门搜索")
	public ResponseMsg findHotSearch(@PathVariable String channel){
		ResponseMsg msg =new ResponseMsg();
		try {
			switch (channel) {
			case "mall":
				msg.setData(indexService.findHotSearch());
				msg.setCode(0);
				break;
			default: 
				return msg.setError("数据不存在");
			}
			
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城首页每日推荐
	 * @author felx
	 * add  20160606
	 */
	@RequestMapping(value="/{channel}/dailyRecommendation",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城首页每日推荐")
	public ResponseMsg getDailyRecommendation(@PathVariable String channel){
		ResponseMsg msg =new ResponseMsg();
		try {
			switch (channel) {
			case "mall":
				msg.setData(indexService.getDailyRecommendation());
				msg.setCode(0);
				break;
			default: 
				return msg.setError("数据不存在");
			}
			
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城首页楼层的横幅广告和分类促销广告
	 * @author felx
	 * add   20160606
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="/{channel}/floorAdvertisement",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城首页楼层的横幅广告和分类促销广告")
	public ResponseMsg getfloorAdvertisement(@PathVariable String channel,
			HttpServletRequest request){
		ResponseMsg msg =new ResponseMsg();
		/*String string = request.getParameter("floorId");
		int floorId=Integer.parseInt(string);
		String positionId = request.getParameter("positionId");*/
		try {
			switch (channel) {
			case "mall":
				msg.setData(indexService.getfloorAdvertisement(null));
				msg.setCode(0);
				break;
			case "wxmall":
				msg.setData(indexService.getfloorAdvertisement("3"));
				msg.setCode(0);
				break;
			case "appmall":
				msg.setData(indexService.getfloorAdvertisement("4"));
				msg.setCode(0);
				break;	
			default: 
				return msg.setError("数据不存在");
			}
			
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城首页通告   add--20160603
	 * @author felx
	 * @param channel
	 * @return
	 */
	@RequestMapping(value="/{channel}/notice",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城首页通告")
	public ResponseMsg findMallNotice(@PathVariable("channel")String channel){
		ResponseMsg msg =new ResponseMsg();
		try {
			switch (channel) {
			case "mall":
				msg.setData(indexService.findMallNotice());
				break;
			default: 
				return msg.setError("数据不存在");
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 商城首页显示订单信息
	 * add--20160603
	 * @author felx
	 */
	@RequestMapping(value="/{channel}/orderMessage",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城首页显示订单信息")
	public ResponseMsg findMallOrderMessage(@PathVariable String channel){
		ResponseMsg msg = new ResponseMsg();
		try {
			switch (channel) {
			case "mall":
				msg.setData(indexService.findMallOrderMessage());
				msg.setCode(0);
				break;
			default: 
				return msg.setError("数据不存在");
			}
			
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 更新商城首页模块：0 热门搜索，1 广告，2 公告，3 楼层广告，4 楼层商品，5 每日推荐 点击量
	 * @author felx
	 */
	@RequestMapping(value="/mall/modular",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "更新商城首页模块：0 热门搜索，1 广告，2 公告，3 楼层广告，4 楼层商品，5 每日推荐 点击量")
	public ResponseMsg updateclickVolume(HttpServletRequest request){	
		ResponseMsg msg = new ResponseMsg();
		try {
			int code = Integer.valueOf(request.getParameter("code"));
			String id = request.getParameter("id");
			indexService.updateclickVolume(code,id);
			msg.setCode(0);	 
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
		
	}
	
	/**
	 * 微信商城查询所有店铺logo和别名
	 */
	@RequestMapping(value="/getAllStoreLogo",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "微信商城查询所有店铺logo和别名")
	public ResponseMsg getAllStoreLogo(){	
		ResponseMsg msg = new ResponseMsg();
		try {
			//微信商城查询所有店铺logo和别名
			msg.setData(indexService.getAllStoreLogo());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
		
	}

	/**
	 * gdb_mall 获取首页楼层的基本信息
	 * @author zheng
	 * @date 2018-09-19
	 */
	@RequestMapping(value="/getFloorInfo")
	@ResponseBody
	@ApiOperation(value = "商城首页楼层基本信息")
	public ResponseMsg getFloorInfo(){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(indexService.findPageFloorDetail(null));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;

	}


    /**
     * gdb_mall 获取首页楼层的基本信息（IOS）
     * @author zheng
     * @date 2018-09-19
     */
    @RequestMapping(value="/getFloorInfoForIos")
    @ResponseBody
    @ApiOperation(value = "商城首页楼层基本信息")
    public ResponseMsg getFloorInfoForIos(){
        ResponseMsg msg = new ResponseMsg();
        try {
            msg.setData(indexService.findPageFloorDetailForIos(null));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;

    }



	/**
	 * gdb_mall 获取首页楼层的商品信息
	 * @author zheng
	 * @date 2018-09-19
	 */
	@RequestMapping(value="/getFloorGoodsInfo")
	@ResponseBody
	@ApiOperation(value = "商城首页楼层商品信息")
	public ResponseMsg getFloorGoodsInfo(@RequestParam("sellerGoodIds")  String sellerGoodIds){
		ResponseMsg msg = new ResponseMsg();
		try {
			for(String id : sellerGoodIds.split(",")){//${}防止sql注入
				if(!StringUtils.isNumericSpace(id)){
					msg.setCode(-1);
					return msg;
				}
			}
			msg.setData(indexService.findFloorGoodsDetail(sellerGoodIds));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}

	/**
	 * gdb_mall 获取首页热卖产品
	 * @author zheng
	 * @date 2018-09-19
	 */
	@RequestMapping(value="/getHotSaleGoodsInfo")
	@ResponseBody
	@ApiOperation(value = "商城首页热卖商品信息")
	public ResponseMsg getHotSaleGoodsInfo(){
		ResponseMsg msg = new ResponseMsg();
		try {
			String sellerGoodIds = indexService.getHotSaleGoodsInfo();
			msg.setData(indexService.findFloorGoodsDetail(sellerGoodIds));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}




}
