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
import bmatser.service.DealerBalanceI;
import bmatser.service.DealerScoreI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("balance")
@Api(value="balance", description="用户余额及使用记录")
public class DealerBalanceController {
	@Autowired
	private DealerBalanceI balanceService;
	/**
	 * 获取用户余额及使用记录
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/getDealerBalance/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = " 获取用户余额及使用记录")
	public ResponseMsg getDealerBalanceByBuyerId(@ApiParam(value="页数") @PathVariable Integer page,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		try {
			LoginInfo login = MemberTools.isSaasLogin(request);
			if(login!=null){
				msg.setData(balanceService.getDealerBalanceByBuyerId(Integer.parseInt(login.getDealerId()), page*10, 10));
			}else{
				return msg.setError("请登录");
			}
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
}
