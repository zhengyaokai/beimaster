/**
 *商品评论
 */
package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.exception.GdbmroException;
import bmatser.model.MemberComment;
import bmatser.service.GoodsCommentI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;

/**
 * @author felx
 * 
 */
@Controller
@RequestMapping("goodsComment")
@Api(value="goodsComment", description="商品评论")
public class GoodsCommentContrller {

	@Autowired
	private GoodsCommentI goodsCommentI;
	/**
	 * 查看商品评价
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/{page}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查看商品评价" )
	public ResponseMsg getGoodsComment(HttpServletRequest request,@ApiParam(value="页数") @PathVariable("page")int page){
		ResponseMsg msg = new ResponseMsg();
		try {
			if(page>0){
				msg.setData(goodsCommentI.getGoodsCommentByPage(request,(page-1)*8,8));
			}
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
		
	}
	/**
	 * 保存评价
	 * @param comment
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "保存评价" )
	public ResponseMsg addGoodsComment(@ModelAttribute MemberComment comment,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			comment.setDealerId(Integer.parseInt(loginInfo.getDealerId()));
			goodsCommentI.addGoodsCommentByPage(comment);
			msg.setCode(0);
		} catch (GdbmroException e) {
			msg.setMsg(e.getMessage());
			msg.setCode(e.getCode());
		}catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	/**
	 * 审核评价
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "审核评价" )
	public ResponseMsg checkGoodsComment(@ApiParam(value="评价ID")Integer id,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			goodsCommentI.checkGoodsComment(id,loginInfo.getDealerId());
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}
	/**
	 * 删除订单评价
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除订单评价" )
	public ResponseMsg delGoodsComment(@ApiParam(value="评价ID")Integer id,HttpServletRequest request){
		ResponseMsg msg = new ResponseMsg();
		LoginInfo loginInfo = MemberTools.isLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			msg.setMsg("请先登录");
			msg.setCode(-1);
			return msg;
		}
		try {
			goodsCommentI.delGoodsComment(id);
			msg.setCode(0);
		} catch (Exception e) {
			msg.setMsg(e.toString());
			msg.setCode(-1);
		}
		return msg;
	}

}
