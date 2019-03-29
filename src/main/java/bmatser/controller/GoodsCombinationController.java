package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.GoodsCombinationI;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("/combination")
@Api(value="combination", description="商品组合套餐")
public class GoodsCombinationController {
	@Autowired
	private GoodsCombinationI combinationService;
	/**
	 * 查询商品组合套餐
	 * @return
	 */
	@RequestMapping(value="/selectGoodsCombination",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询商品组合套餐")
	public ResponseMsg  selectGoodsCombination(@ApiParam(value="sellerGoodsId") @RequestParam String sellerGoodsId, HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(combinationService.selectGoodsCombination(sellerGoodsId));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
}
