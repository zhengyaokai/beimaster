package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.service.GoodsActivityI;
import bmatser.service.GoosActivityNoticeI;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("xsms")
@Api(value="xsms", description="活动")
public class GoodsActivityController {

	@Autowired
	private GoodsActivityI activityService;
	
	@Autowired
	private GoosActivityNoticeI activityNoticeService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取进行中的特卖活动" )
	public ResponseMsg findGoodsActivity(@ApiParam(value="活动渠道（1：saas 2：商城 3 : 店铺活动）") @RequestParam(required=false) String activityChannel,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		/*String activityChannel = request.getParameter("activityChannel");*/
		Integer channel = StringUtils.isNotBlank(activityChannel)?Integer.valueOf(activityChannel):null;
		try {
			msg.setData(activityService.findGoodsActivity(channel));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * saas获取楼层活动
	 * @author felx
	 */
	@RequestMapping(value="/getXsmsActivity",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas获取楼层活动" )
	public ResponseMsg getFloorGoodsActivity(){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(activityService.getFloorGoodsActivity());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	@RequestMapping(value="/goods/{goodsActivityId}/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取特卖产品")
	public ResponseMsg findGoodsActivitySales(@ApiParam(value="商品活动ID") @PathVariable int goodsActivityId,
			@ApiParam(value="页数") @PathVariable int page, 
			@ApiParam(value="分类id") @RequestParam(required=false) Integer cateId,
			@ApiParam(value="销售价格排序字段") @RequestParam(required=false) String priceOrder,
			@ApiParam(value="折扣率排序字段") @RequestParam(required=false) String rateOrder,
			@ApiParam(value="活动渠道（1：saas 2：商城 3 : 店铺活动）") @RequestParam(required=false) String activityChannel,HttpServletRequest request){
		int rows = 10;
		String row = request.getParameter("rows");
		if(StringUtils.isNotBlank(row)){
			rows = Integer.parseInt(row);
		}
		/*String activityChannel = request.getParameter("activityChannel");//活动渠道1：saas  2：商城
*/		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(activityService.findGoodsActivitySales(goodsActivityId, cateId, priceOrder, rateOrder, page*rows, rows,activityChannel));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城特卖商品
	 * @author felx
	 */
	@RequestMapping(value="/mallSales", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "商城特卖商品")
	public ResponseMsg findMallActivityGoods(HttpServletRequest request){
		String activityChannel = "2";//活动渠道1：saas  2：商城
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(activityService.findMallActivityGoods(activityChannel));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/notice", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取特卖预告")
	public ResponseMsg findGoodsActivityNotices() {
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(activityNoticeService.findGoodsActivityNotices());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	@RequestMapping(value="/category/{goodsActivityId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取特卖商品的分类")
	public ResponseMsg findGoodsActivityCategory(@ApiParam(value="商品活动ID") @PathVariable int goodsActivityId) {
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(activityService.findGoodsActivityCategory(goodsActivityId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * @author felx
	 * 下期预告
	 */
	@RequestMapping(value="/goodsActivityNotice", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "下期预告")
	public ResponseMsg selectUnderlineGoodsActivity() {
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(activityNoticeService.selectUnderlineGoodsActivity());
			msg.setCode(0);
			msg.setMsg("查询下期预告数据成功");
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
}
