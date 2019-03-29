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

import bmatser.pageModel.PageMode;
import bmatser.service.SysMessageI;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("sysMessage")
@Api(value="sysMessage", description="贷通信息")
public class SysMessageController {
	@Autowired
	private SysMessageI messageService;
	/**
	 * saas贷通信息列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value={"/getSysMessageList/{page}","/app/getSysMessageList/{page}"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas贷通信息列表",
	notes = "saas贷通信息列表")
	public ResponseMsg getSysMessageList(@ApiParam(value="页数") @PathVariable Integer page,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		if(page<1){
			return msg.setError("程序参数错误");
		}else{
			page = page - 1;
		}
		PageMode pageMode = new PageMode(request);
		Integer dealerId;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
			msg.setData(messageService.getSysMessageList(dealerId,page*10, 10));
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * saas贷通未读信息总数
	 * @return
	 */
	@RequestMapping(value={"/getUnreadSysMessageCount","/app/getUnreadSysMessageCount"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas贷通未读信息总数",
	notes = "saas贷通未读信息总数")
	public ResponseMsg getUnreadSysMessageCount(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		Integer dealerId;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
			msg.setData(messageService.getUnreadSysMessageCount(dealerId));
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * saas贷通信息列表批量删除
	 * @return
	 */
	@RequestMapping(value="/deleteSysMessageById", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas贷通信息列表批量删除",
	notes = "saas贷通信息列表批量删除")
	public ResponseMsg deleteSysMessageById(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String ids = pageMode.getStringValue("ids");
		try {
			messageService.deleteSysMessageById(ids);
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
	/**
	 * saas贷通信息列表批量更新已读
	 * @return
	 */
	@RequestMapping(value={"/updateIsRead","/app/updateIsRead"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas贷通信息列表批量删除",
	notes = "saas贷通信息列表批量删除")
	public ResponseMsg updateIsRead(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String ids = pageMode.getStringValue("ids");
		Integer dealerId;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
			messageService.updateIsRead(dealerId,ids);
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * saas贷通信息点击查看
	 * @return
	 */
	@RequestMapping(value={"/getSysMessageInfo","/app/getSysMessageInfo"}, method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "saas贷通信息点击查看",
	notes = "saas贷通信息点击查看")
	public ResponseMsg getSysMessageInfo(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		String id = pageMode.getStringValue("id");
		Integer dealerId;
		try {
			switch (pageMode.channel()) {
			case "app":
				dealerId = Integer.parseInt(pageMode.getAppLogin().getDealerId());
				break;
			default:
				dealerId = Integer.parseInt(pageMode.getSaasLogin().getDealerId());
				break;
			}
			msg.setData(messageService.getSysMessageInfo(dealerId,id));
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
}
