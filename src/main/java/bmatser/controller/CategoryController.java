package bmatser.controller;

import bmatser.service.BrandI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;

import bmatser.model.Brand;
import bmatser.model.Category;
import bmatser.model.ModelSelectionCategory;
import bmatser.pageModel.PageMode;
import bmatser.service.CategoryI;
import bmatser.service.GoodsI;
import bmatser.util.ResponseMsg;
/**
 * 查询所有存在上架商品的系统分类
 * @author felx
 */
@Controller
@RequestMapping("category")
@Api(value="category", description="分类")
public class CategoryController {

	@Autowired
	private CategoryI categoryIService;
	@Autowired
	private GoodsI goodsService;

	@Autowired
    private BrandI brandI;

	/**
	 *查询所有存在上架商品的系统分类
	 */
	@RequestMapping(value="getCates",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取SAAS所有上架商品的系统分类", response = Category.class, 
		notes = "获取SAAS所有上架商品的系统分类，根据商品类型区分")
	public ResponseMsg selectCategory(HttpServletRequest request,
			@ApiParam(value="商品类型") @RequestParam(required=false) String type){
		ResponseMsg res = new ResponseMsg();
//		String param=request.getParameter("goodsInitial");
		try {
			List categoryTotal=categoryIService.selectCategory(type);
			res.setData(categoryTotal);
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	
	/**
	 *根据品牌获取选型分类
	 */
	@RequestMapping(value="/getSelectionCates",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据品牌获取选型分类",notes = "获取SAAS所有上架商品的系统分类，根据商品类型区分",response = Object.class)
	public Object getSelectionCates(HttpServletRequest request,
			@ApiParam(value="品牌ID") @RequestParam(required=false) Integer brandId){
			List<ModelSelectionCategory> list = categoryIService.selectModelSelectionCates(brandId);
			return list;
		
	}

    /**
     *根据品牌获取选型分类 create by yr on 2018-11-16
     */
    @RequestMapping(value="/getSelectionCatesForIos",method=RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据品牌获取选型分类",notes = "获取SAAS所有上架商品的系统分类，根据商品类型区分",response = Object.class)
    public Object getSelectionCatesForIos(HttpServletRequest request,
                                    @ApiParam(value="品牌ID") @RequestParam(required=false) String brandId){
        String[] brandIs = new String[]{} ;
        ResponseMsg msg = new ResponseMsg();
        List list = new ArrayList();
        if(StringUtils.isNotBlank(brandId)){
             brandIs = brandId.split(",");
            try {
                for(int i=0;i<brandIs.length;i++){
                    Integer.parseInt(brandIs[i]);
                    List<ModelSelectionCategory> resultList = categoryIService.selectModelSelectionCates(Integer.parseInt(brandIs[i]));
                    list.add(resultList);
                }
                msg.setData(list);
                msg.setCode(0);
            }catch (Exception e){
                msg.setCode(-1);
                msg.setData(e.getMessage());
            }
        }else{
          List<Map<String,Object>> brandLists = brandI.findVaildBrandForIos();
            for (Map<String,Object> brandMap:brandLists) {
                if(brandMap.size()>0&&brandMap!=null){
                    if(brandMap.get("id")!=null){
                        if(StringUtils.isNotBlank(brandMap.get("id").toString())){
                            List<ModelSelectionCategory> resultList = categoryIService.selectModelSelectionCates(Integer.parseInt(brandMap.get("id").toString()));
                            list.add(resultList);
                        }
                    }else{
                        msg.setData("brandId 获取失败");
                        msg.setCode(-1);
                    }
                }else{
                    msg.setData("brand 获取失败");
                    msg.setCode(-1);
                }
            }
            msg.setData(list);
            msg.setCode(0);
        }
        return msg;
    }



	/**
	 *查询所有存在上架商品的系统分类
	 */
	@RequestMapping(value="/app/selectCategory",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询所有存在上架商品的系统分类", response = Category.class)
	public ResponseMsg selectAppCategory(@ApiParam(value="商品类型") @RequestParam(required=false)String goodsInitial,HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
		String param=goodsInitial;
		try {
			List categoryTotal=categoryIService.selectAppCategory(param);
			res.setData(categoryTotal);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	/**
	 * saas一级分类
	 * @param request
	 * @return
	 */
	@RequestMapping(value="topCategory",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取SAAS一级分类", response = Category.class, notes = "获取SAAS一级分类，根据商品类型区分" , produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseMsg getTopCategory(HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
		try {
			res.setData(categoryIService.getTopCategory());
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	/**
	 * 分类下品牌
	 * @param request
	 * @return
	 */
	@RequestMapping(value="brand",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = " 分类下品牌", response = Brand.class)
	public ResponseMsg getBrand(@ApiParam(value="分类ID") @RequestParam(required=false) String cateId,HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
		PageMode model = new PageMode(request);
		try {
			res.setData(categoryIService.findCateBrand(model));
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	/**
	 * saas所有品牌分类下的子分类
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getAllCate",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = " saas所有品牌分类下的子分类", response = Category.class)
	public ResponseMsg getAllCate(@ApiParam(value="分类ID") @RequestParam(required=false) String cateId,
			@ApiParam(value="品牌ID") @RequestParam(required=false) String brandId,HttpServletRequest request){
		ResponseMsg res = new ResponseMsg();
/*		String cateId = request.getParameter("cateId");
		String brandId = request.getParameter("brandId");*/
		try {
			res.setData(categoryIService.getChildAllCate(cateId,brandId));
			res.setCode(0);
		} catch (Exception e) {
			res.setError(e);
		}
		return res;
	}
	

}
