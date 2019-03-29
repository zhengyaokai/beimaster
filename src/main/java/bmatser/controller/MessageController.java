package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import bmatser.util.alibaba.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import bmatser.service.SysMessageI;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("/notice")
@Api(value="message", description="消息")
public class MessageController {

	@Autowired
	private SysMessageI messageService;
	
	@RequestMapping(value="/datanews", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "公告")
	public ResponseMsg findDataMessages(){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(messageService.findDataMessages());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	/**
	 * Saas通知公告
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Saas通知公告")
	public ResponseMsg findDataNotice(){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(messageService.findDataNotice());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}


    @RequestMapping(value = "/details",method=RequestMethod.GET)
    @ResponseBody
    public ResponseMsg findDataNoticeDetails(int page){
        ResponseMsg msg = new ResponseMsg();
        try {
            if(page<1){
                return msg.setError("程序参数错误");
            }else{
                page = page - 1;
            }
            int rows = 10;
            msg.setData(messageService.findDataNoticeDetails(page*rows,rows));
            msg.setCode(0);
        } catch (Exception e){
            msg.setError(e);
        }
        return msg;
    }

/**
 * create by yr on 2018-11-26
 */

@RequestMapping(value = "/detailsForIos",method=RequestMethod.GET)
@ResponseBody
public ResponseMsg findDataNoticeDetailsForIos(int page){
    ResponseMsg msg = new ResponseMsg();
    try {
        if(page<1){
            return msg.setError("程序参数错误");
        }else{
            page = page - 1;
        }
        int rows = 10;
        msg.setData(messageService.findDataNoticeDetailsForIos(page*rows,rows));
        msg.setCode(0);
    } catch (Exception e){
        msg.setError(e);
    }
    return msg;
}



    /**
     * Saas通知公告ForIOS
     * @return
     */
    @RequestMapping(value = "/noticeForIos",method=RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Saas通知公告")
    public ResponseMsg findDataNoticeForIos(){
        ResponseMsg msg = new ResponseMsg();
        try {
            msg.setData(messageService.findDataNoticeForIos());
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }




    /**
	 * 查询通告详情 
	 * @return title标题，content内容 ，create_time发布时间 ，message_type消息类型（1：数据更新 2：公告 3:移动端消息推送）
	 */
	@RequestMapping(value="/selectSysMessage", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询通告详情")
	public ResponseMsg selectSysMessage(HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		try {
			msg.setData(messageService.selectSysMessage(request));
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
    /**
     * 获得系统公告 create by yr on 2018-11-05
     */

    @RequestMapping(value = "/getSysNoticeForIos/{page}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg getSysNotice( @PathVariable Integer page){
        Integer rows = 10;
        if(page==0||page==null){
            page = 1;
        }
        ResponseMsg msg = new ResponseMsg();
        try{
            msg.setData(messageService.getSysNotice((page-1)*rows,rows));
            msg.setCode(0);
        }catch (Exception e){
            msg.setData(e.getMessage());
            msg.setCode(-1);
        }
        return msg;
    }

    @RequestMapping(value = "/getSysNoticeForIosById",method = RequestMethod.GET)
    @ResponseBody
    public ResponseMsg getSysNoticeById( String id){
        Integer ids = 0 ;
        if(StringUtils.isNotBlank(id)){
             ids = Integer.valueOf(id);
        }
        ResponseMsg msg = new ResponseMsg();

        try{
            msg.setData(messageService.getSysNoticeById(ids));
            msg.setCode(0);
        }catch (Exception e){
            msg.setData(e.getMessage());
            msg.setCode(-1);
        }
        return msg;
    }

}
