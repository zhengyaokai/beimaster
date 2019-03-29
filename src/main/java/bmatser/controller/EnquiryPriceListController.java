package bmatser.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import bmatser.model.AdverImage;
import bmatser.model.EnquiryPrice;
import bmatser.model.EnquiryPriceGoods;
import bmatser.service.EnquiryPriceListI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
@Controller
@RequestMapping("enquiry_price")
@Api(value="enquiryPriceList", description="我的询价列表")
public class EnquiryPriceListController {
	
	@Autowired
	EnquiryPriceListI enquiryPriceListI;

	/**
	 * 查询询价单列表
	 *	
	 * @return
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询我的询价单列表", response = EnquiryPrice.class)
	public ResponseMsg findEnquiryPriceList(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		List<EnquiryPrice> list = enquiryPriceListI.findEnquiryPriceList(Integer.parseInt(loginInfo.getDealerId()));
		msg.setData(list);
		return msg;
	}
	
	
	/**
	 * 查询询价单详情
	 *	
	 * param enquiry_pirce id
	 */
	@RequestMapping(value="/info/{id}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询询价单详情", response = EnquiryPrice.class)
	public ResponseMsg findAllAdverImages(@ApiParam(value="询价单id") @PathVariable("id")Integer id){
		ResponseMsg msg = new ResponseMsg();
		msg.setData(enquiryPriceListI.findEnquiryPriceGoods(id));
		return msg;
	}
	
}
