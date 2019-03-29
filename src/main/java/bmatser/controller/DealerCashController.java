package bmatser.controller;

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

import bmatser.model.Category;
import bmatser.model.DealerCash;
import bmatser.service.DealerCashI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("voucher")
@Api(value="Voucher", description="抵扣券")
public class DealerCashController {

	@Autowired
	private DealerCashI cashService;
	/**
	 * 现金券列表(app)
	 * @param buyerId
	 * @param page
	 * @return
	 */
//	@RequestMapping(value="/{buyerId}/{page}", method=RequestMethod.GET)
//	@ResponseBody
//	@ApiOperation(value = "现金券列表(app)", response = DealerCash.class)
//	public ResponseMsg findByBuyerId(@ApiParam(value="商家ID")@PathVariable Integer buyerId,@ApiParam(value="页数") @PathVariable Integer page){
//		ResponseMsg msg = new ResponseMsg();
//		try {
//			msg.setData(cashService.findByBuyerId(buyerId, page*20,20));
//			msg.setCode(0);
//		} catch (Exception e) {
//			msg.setMsg(e.toString());
//			msg.setCode(-1);
//		}
//		return msg;
//	}
	/**
	 * 现金券列表（saas）
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saas/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "现金券列表（saas）", response = DealerCash.class)
	public ResponseMsg findSaasByBuyerId(@ApiParam(value="页数")@PathVariable Integer page,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		try {
			LoginInfo login = MemberTools.isSaasLogin(request);
			if(login!=null){
				msg.setData(cashService.findByBuyerId(Integer.parseInt(login.getDealerId()), page*10,10));
			}else{
				return msg.setError("请登录");
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * 商城现金券
	 * @author felx
	 */
//	@RequestMapping(value="/mall/{page}", method=RequestMethod.GET)
//	@ResponseBody
//	@ApiOperation(value = "商城现金券", response = DealerCash.class)
//	public ResponseMsg findMallByBuyerId(@ApiParam(value="页数")@PathVariable Integer page,HttpServletRequest request){
//		ResponseMsg msg = new ResponseMsg();
//		try {
//			LoginInfo loginInfo = MemberTools.isLogin(request);
//			if(null == loginInfo || null == loginInfo.getDealerId()){
//				msg.setMsg("请先登录");
//				msg.setCode(-1);
//				return msg;
//			}
//			msg.setData(cashService.findByBuyerId(Integer.valueOf(loginInfo.getDealerId()), page*20,20));
//			msg.setCode(0);
//		} catch (Exception e) {
//			msg.setMsg(e.toString());
//			msg.setCode(-1);
//		}
//		return msg;
//	}
}
