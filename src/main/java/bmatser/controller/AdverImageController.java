package bmatser.controller;
import java.util.List;

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

import bmatser.model.AdverImage;
import bmatser.model.Category;
import bmatser.service.AdverImageI;
import bmatser.service.CorpusKeysServiceI;
import bmatser.util.JSONUtil;
import bmatser.util.ResponseMsg;
@Controller
@RequestMapping("adverBanner")
@Api(value="adverBanner", description="banner图片")
public class AdverImageController {

	@Autowired
	private AdverImageI imageService;
	
	@Autowired
	private CorpusKeysServiceI corpusKeysServiceI;

	/**
	 * 查询所有banner图片
	 *	/image 手机端
	 *	/list pc 端
	 * /mall 商城
	 * @return
	 */
	@RequestMapping(value="/{channel}/{positionId}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询所有banner图片", response = AdverImage.class)
	public ResponseMsg findAllAdverImages(@ApiParam(value="image;手机端 :list pc 端 :mall 商城") @PathVariable("channel")String channel,
			@ApiParam(value="放置位置（1：特价区banner 2：厂家直发区banner 3：列表页banner 4：商城首页banner 5：活动汇banner）") @PathVariable("positionId") String positionId){
		ResponseMsg msg = new ResponseMsg();
		try {
			Object data = null;
			switch (channel) {
			case "sale_image":
				if(StringUtils.isBlank(positionId)){
					data = imageService.findAllAdverImages(0);
					
				}else{
					data = imageService.findAllAdverImages(Integer.parseInt(positionId));
				}
				
				break;
			case "home_index":
				if(StringUtils.equals("index", positionId)){
					data = imageService.findAdverImages(1);
				}else{
					data = imageService.findAdverImages(Integer.parseInt(positionId));
				}
				
				break;
//			case "mall":
//				data = imageService.findMallAdverImages(positionId);
//				break;
//			case "appMall":
//				data = imageService.findAppMallAdverImages(positionId);
//				break;
			default:
				break;
			}
			msg.setData(data);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
//	/**
//	 * 查询积分商城所有banner图片
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/jfBanner/{positionId}", method=RequestMethod.GET,produces="text/html;charset=UTF-8")
//	@ApiOperation(value = "查询积分商城所有banner图片", response = AdverImage.class)
//	public String findJfAllAdverImages(@ApiParam(value="jsonp返回") @RequestParam String callback,
//			@ApiParam(value="放置位置（1：特价区banner 2：厂家直发区banner 3：列表页banner 4：商城首页banner 5：活动汇banner 6：积分商城banner）") @PathVariable("positionId") int positionId){
//		ResponseMsg msg = new ResponseMsg();
//		try {
//			msg.setData(imageService.findAdverImages(positionId));
//			msg.setCode(0);
//		} catch (Exception e) {
//			msg.setError(e);
//		}
//		return callback + "(" + JSONUtil.objectToJson(msg).toString() + ")";
//	}
	
}
