/**
 * 订单运费
 */
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

import bmatser.service.FreightAmountI;
import bmatser.util.ResponseMsg;

/**
 * @author felx
 * 
 */
@Controller
@RequestMapping("freight")
@Api(value="freight", description="订单运费")
public class FreightAmountController {

	@Autowired
	private FreightAmountI freightAmountI;
	
	
	/**
	 * 获取运费金额
	 * @param req
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取运费金额")
	public ResponseMsg getFreightAmount(@ApiParam(value="购物车ID") @RequestParam String cartIds,
			@ApiParam(value="收货地址ID") @RequestParam String buyerAddrId,HttpServletRequest req){
		ResponseMsg msg = new ResponseMsg();
		/*String cartIds = req.getParameter("cartIds");
		String buyerAddrId = req.getParameter("buyerAddrId");*/
		try {
			msg.setData(freightAmountI.getFreightAmount(cartIds,buyerAddrId));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
		
	}
}
