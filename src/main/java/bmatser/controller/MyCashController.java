/**
 * 
 */
package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.service.MyCashI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

/**
 * @author felx
 * 
 */
@Controller
@RequestMapping("myCash")
@Api(value="myCash", description="我的现金券")
public class MyCashController {
	
	@Autowired
	private MyCashI myCashI;
	/**
	 * 现金券列表(mall)
	 * @param req
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	@ResponseBody
	@ApiOperation(value = "现金券列表(mall)")
	public ResponseMsg  findMyCash(HttpServletRequest  req,@ApiParam(value="页数")@PathVariable int page){
		ResponseMsg msg = new ResponseMsg();
		try {
			LoginInfo login = MemberTools.isLogin(req);
			if(login!=null){
				msg.setData(myCashI.findMyCash(login.getDealerId(),page));
			}else{
				throw new Exception("请登录");
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e); 
		}
		return msg;
	}
	/**
	 * 现金券列表(saas)
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping("/saas/{page}")
	@ResponseBody
	@ApiOperation(value = "现金券列表(saas)")
	public ResponseMsg  findSaasMyCash(HttpServletRequest  request,@ApiParam(value="页数") @PathVariable int page){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}
		try {
			LoginInfo login = MemberTools.isSaasLogin(request);
			if(login!=null){
				msg.setData(myCashI.findMyCash(login.getDealerId(),page-1));
			}else{
				return msg.setError("请登录");
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e); 
		}
		return msg;
	}
}
