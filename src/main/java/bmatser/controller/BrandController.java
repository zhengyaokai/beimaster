package bmatser.controller;
/**
 * 商品的品牌
 */
import javax.servlet.http.HttpServletRequest;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.annotations.ResponseDefault;
import bmatser.model.ApplicationApp;
import bmatser.model.Brand;
import bmatser.service.BrandI;
import bmatser.util.ResponseMsg;

//@RestController
@Controller
@RequestMapping("brand")
@Api(value="brand", description="商品的品牌")
public class BrandController {

	@Autowired
	private BrandI brandService;
	/**
	 * 获取已上架商品的品牌
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取已上架商品的品牌", response = Brand.class)
	public ResponseMsg findBrands(){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(brandService.findBrands());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}


	/**
	 * 查询精选品牌
	 * @return
	 */
	@RequestMapping(value="/hotBrand",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询精选品牌", response = Brand.class)
	@ResponseDefault
	public Object findChoiceBrands(){
		return brandService.findChoiceBrands();
	}


    /**
     * 查询精选品牌
     * @return
     */
    @RequestMapping(value="/hotBrandForIos",method=RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询精选品牌", response = Brand.class)
    @ResponseDefault
    public Object findChoiceBrandsForIos(){
                Map<String,Object>  map = brandService.findChoiceBrandsForIos();
                return map ;
    }






	/**
	 * 标记类型查找品牌
	 * @param markType
	 * @return
	 */
	@RequestMapping(value="/mark/{markType}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "标记类型查找品牌", response = Brand.class)
	public ResponseMsg findBrandsByMark(@ApiParam(value="标记类型（1：新增  2：热门  3：推荐）")@RequestParam int markType){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(brandService.findBrandsByMark(markType));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 查询出当前在售的所有品牌
	 * @return
	 */
	@RequestMapping(value={"/selectForSaleBrands","/app/selectForSaleBrands"},method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询出当前在售的所有品牌", response = Brand.class)
	@ResponseDefault
	public Object selectForSaleBrands(@ApiParam(value="1:saas上架  2:面价,3:appmall")@RequestParam(required=false) String goodsInitial,HttpServletRequest request){
		int code = goodsInitial!=null?Integer.parseInt(goodsInitial):0;
		StringBuffer url = request.getRequestURL();
		if(url.indexOf("/app/")>=0){
			return brandService.selectForAppSaleBrands(code);
		}else{
			return brandService.selectForSaleBrands(code);
		}
	}
}
