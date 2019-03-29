/**
 * 店铺列表
 */
package bmatser.controller;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.service.StoreListI;
import bmatser.util.ResponseMsg;

/**
 * @author felx
 * 
 */
@Controller
@RequestMapping("storeList")
@Api(value="storeList", description="店铺列表")
public class StoreListController {

	@Autowired
	private StoreListI storeListI;
	
	/**
	 * 通过别名进入店铺
	 * @param alias
	 * @return
	 */
	@RequestMapping(value="/list/{page}/{row}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "通过别名进入店铺", 
	notes = "通过别名进入店铺")
	@ApiParam(required=false,name="keyword",value="别名") 
	public ResponseMsg findStoreInfo(@PathVariable("page")int page,@PathVariable("row")int row,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		try {
			String keyword = request.getParameter("keyword");
			if(StringUtils.isNotBlank(keyword)){
				keyword = URLDecoder.decode(keyword,"UTF-8");
			}
			msg.setData(storeListI.findStoreList((page-1)*row,row,keyword));
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
}
