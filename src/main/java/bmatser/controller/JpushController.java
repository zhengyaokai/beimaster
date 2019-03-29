package bmatser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import bmatser.pageModel.JpushPage;
import bmatser.service.JpushI;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("jpush")
@Api(value="jpush", description="消息")
public class JpushController {
	@Autowired
	private JpushI jpushI;
	
	@RequestMapping(value="/send", method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "发送消息")
	ResponseMsg sendMessage(JpushPage jpushPage){
		ResponseMsg msg = new ResponseMsg();
		try {
			jpushI.sendMessage(jpushPage);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setCode(-1);
			msg.setMsg(e.toString());
		}
		return msg;
	}
}
