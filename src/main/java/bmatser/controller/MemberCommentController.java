package bmatser.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.annotations.Login;
import bmatser.annotations.ResponseDefault;
import bmatser.model.LoginInfoUtil;
import bmatser.model.MemberComment;
import bmatser.service.MemberCommentI;
import bmatser.service.OrderInfoI;

@Controller
@RequestMapping("memberComment")
@Api(value="memberComment", description="评价")
public class MemberCommentController {
	@Autowired
	private MemberCommentI memberCommentI;
	@Autowired
	private OrderInfoI orderService;
	/**
	 * saas订单确认收货后评价
	 */
	  @RequestMapping(value={"/insertMemberComment"}, method={RequestMethod.POST})
	  @ResponseBody
	  @ApiOperation(value="saas订单确认收货后评价", notes="saas订单确认收货后评价")
	  @Login(saas="/insertMemberComment")
	  @ResponseDefault
	  public Object insertMemberComment
	  (HttpServletRequest request,@ModelAttribute MemberComment memberComment,LoginInfoUtil l)
	    throws Exception
	  {
		  memberComment.setDealerId(Integer.valueOf(l.getDealerId()));
		Timestamp orderTime = orderService.getOrderTime(memberComment.getOrderId());  
		if(orderTime!=null){
			memberComment.setBuyTime(orderTime);
		}
		memberCommentI.insertMemberComment(memberComment);
	    return null;
	  }
	  
	  /**
		 * saas订单查看评价
		 */
	  @RequestMapping(value={"/selectMemberComment"}, method={RequestMethod.GET})
	  @ResponseBody
	  @ApiOperation(value="saas订单查看评价", notes="saas订单查看评价",response = MemberComment.class)
	  @Login(saas="/selectMemberComment")
	  @ResponseDefault
	  public Object selectMemberComment
	  (HttpServletRequest request,@ModelAttribute MemberComment memberComment)
	    throws Exception
	  {
	    return memberCommentI.selectMemberComment(memberComment);
	  }
}
