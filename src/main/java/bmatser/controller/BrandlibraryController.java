/**
 * 品牌库页面
 */
package bmatser.controller;

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

import bmatser.model.Brand;
import bmatser.model.Category;
import bmatser.model.SellerGoods;
import bmatser.service.BrandlibraryI;
import bmatser.util.ResponseMsg;

/**
 * @author felx
 * 
 */
@Controller
@RequestMapping("brandlibrary")
@Api(value="brandlibrary", description="品牌库页面")
public class BrandlibraryController {
	
	@Autowired
	private BrandlibraryI brandlibraryI;
	/**
	 * 查询所有有效品牌
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询所有有效品牌", response = Brand.class)
	public ResponseMsg findBrandlibrary(){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(brandlibraryI.findBrandlibrary());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("查询错误" + e.getMessage());
		}
		return msg;
		
	}
	/**
	 * 查询八大类
	 * @return
	 */
	@RequestMapping(value="/cate",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询八大类", response = Category.class)
	public ResponseMsg findBrandlibraryCate(){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(brandlibraryI.findBrandlibraryCate());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 查询该品牌下商品
	 * @param brandId
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/{brandId}/{page}",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询该品牌下商品", response = SellerGoods.class)
	public ResponseMsg findBrandlibraryGoods(@ApiParam(value="品牌id") @PathVariable("brandId")String brandId,@ApiParam(value="页数") @PathVariable("page")int page){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(brandlibraryI.findBrandlibraryGoods(brandId,page));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("查询错误" + e.getMessage());
		}
		return msg;
		
	}
	/**
	 * 查看该品牌明细
	 * @param brandId
	 * @return
	 */
	@RequestMapping(value="/{brandId}",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看该品牌明细", response = Brand.class)
	public ResponseMsg findBrandlibraryInfo(@ApiParam(value="品牌id") @PathVariable("brandId")String brandId){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(brandlibraryI.findBrandlibraryInfo(brandId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg("查询错误" + e.getMessage());
		}
		return msg;
		
	}
}
