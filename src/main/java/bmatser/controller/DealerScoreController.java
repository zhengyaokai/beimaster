package bmatser.controller;

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

import bmatser.model.DealerCash;
import bmatser.model.DealerScore;
import bmatser.pageModel.PageMode;
import bmatser.service.DealerScoreI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("score")
@Api(value="score", description="积分")
public class DealerScoreController {

	@Autowired
	private DealerScoreI scoreService;
	/**
	 * 积分列表（app）
	 * @param buyerId
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/{buyerId}/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "积分列表（app）", response = DealerScore.class)
	public ResponseMsg findByBuyerId(@ApiParam(value="商家ID")@PathVariable Integer buyerId,@ApiParam(value="页数")  @PathVariable Integer page){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(scoreService.findByBuyerId(buyerId, page*10, 10));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	/**
	 * 获取用户成长值
	 * @param page
	 * @return
	 */
	@RequestMapping(value={"/growthValue/{page}","/app/growthValue/{page}"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取用户成长值", response = DealerScore.class)
	public ResponseMsg getGrowthValueByBuyerId(@ApiParam(value="页数")  @PathVariable Integer page,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		PageMode pageMode = new PageMode(request);
		Integer dealerId;
		Integer rows = StringUtils.isNotBlank(pageMode.getStringValue("rows"))?Integer.parseInt(pageMode.getStringValue("rows")) : 10;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
			msg.setData(scoreService.getGrowthValueByBuyerId(dealerId, page*rows, rows));
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saas/{page}", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas获取用户成长值", response = DealerScore.class)
	public ResponseMsg findSaasByBuyerId(@ApiParam(value="页数") @PathVariable Integer page,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		try {
			LoginInfo login = MemberTools.isSaasLogin(request);
			if(login!=null){
				msg.setData(scoreService.findByBuyerId(Integer.parseInt(login.getDealerId()), page*10, 10));
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
