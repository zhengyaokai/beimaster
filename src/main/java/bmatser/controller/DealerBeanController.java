package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

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

import bmatser.model.Category;
import bmatser.model.DealerBean;
import bmatser.pageModel.PageMode;
import bmatser.service.DealerBeanI;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("bean")
@Api(value="bean", description="工币")
public class DealerBeanController {

	@Autowired
	private DealerBeanI beanService;
	/**
	 * 工币列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value={"/getDealerBeanList/{page}","/app/getDealerBeanList/{page}"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "工币列表", response = DealerBean.class)
	public ResponseMsg getDealerBeanList(@ApiParam(value="页数")  @PathVariable Integer page,
			HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		PageMode pageMode = new PageMode(request);
		Integer dealerId;
		/*String status  = pageMode.getStringValue("status");*///状态（1：有效 2：扣除  3：冻结  4：删除）
		//Integer rows = StringUtils.isNotBlank(pageMode.getStringValue("rows"))?Integer.parseInt(pageMode.getStringValue("rows")) : 20;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
			msg.setData(beanService.getDealerBeanList(dealerId,page*10, 10));
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}

	/**
	 * 冻结工币列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value={"/getFreezeDealerBeanList/{page}","/app/getFreezeDealerBeanList/{page}"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "冻结工币列表", response = DealerBean.class)
	public ResponseMsg getFreezeDealerBeanList(@ApiParam(value="页数")  @PathVariable Integer page,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		PageMode pageMode = new PageMode(request);
		Integer dealerId;
		/*String status  = pageMode.getStringValue("status");*///状态（1：有效 2：扣除  3：冻结  4：删除）
		//Integer rows = StringUtils.isNotBlank(pageMode.getStringValue("rows"))?Integer.parseInt(pageMode.getStringValue("rows")) : 20;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
			msg.setData(beanService.getFreezeDealerBeanList(dealerId,page*10, 10));
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
}
