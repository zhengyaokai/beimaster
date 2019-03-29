/**
 * 
 */
package bmatser.service.impl;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.GoodsCommentMapper;
import bmatser.exception.GdbmroException;
import bmatser.model.MemberComment;
import bmatser.service.GoodsCommentI;

/**
 *	@create_date 2016-2-18
 * 
 */
@Service("goodsCommentService")
public class GoodsCommentImpl implements GoodsCommentI {

	@Autowired
	private GoodsCommentMapper commentMapper;
	
	@Override
	public Map getGoodsCommentByPage(HttpServletRequest request, int page,
			int row) throws Exception {
		Map resultData = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(request.getParameter("id")) && StringUtils.isBlank(request.getParameter("sellerGoodsId")) &&
			StringUtils.isBlank(request.getParameter("orderId"))&&StringUtils.isBlank(request.getParameter("dealerId"))){
				throw new Exception("参数错误");
		}
		queryMap.put("id", request.getParameter("id"));
		queryMap.put("sellerGoodsId", request.getParameter("sellerGoodsId"));
		queryMap.put("orderId", request.getParameter("orderId"));
		queryMap.put("dealerId", request.getParameter("dealerId"));
		queryMap.put("page", page);
		queryMap.put("row", row);
		resultData.put("commentList", commentMapper.getGoodsCommentByPage(queryMap));
		resultData.put("rows", commentMapper.getGoodsCommentByPageColunt(queryMap));
		return resultData;
	}

	@Override 
	public void addGoodsCommentByPage(MemberComment comment) throws Exception {
		Map map = commentMapper.isExistGetOrder(comment.getDealerId(), comment.getSellerGoodsId());
		if(map != null && !map.isEmpty()){
			Long id = Long.parseLong(map.get("id").toString());
			String date = map.get("orderTime").toString();
			Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = (Date) f.parseObject(date);
            Timestamp time = new Timestamp(d.getTime());
			comment.setOrderId(id);
		}else{
			throw new GdbmroException(32002,"请购买后评价");
		}
		if(commentMapper.isExistGoods(comment)==0){
			throw new GdbmroException(32002,"该订单商品不存在");
		}
		if(commentMapper.isExist(comment)>0){
			throw new GdbmroException(32002,"该订单已评价");
		}
		commentMapper.add(comment);
		commentMapper.updateOrderGoods(comment.getOrderId(), comment.getSellerGoodsId());
	}

	@Override
	public void checkGoodsComment(Integer id,String dealerId) throws Exception {
		if(commentMapper.isCheck(id)>0){
			throw new Exception("该评价已审核");
		}
		commentMapper.check(id,dealerId);
	}

	@Override
	public void delGoodsComment(Integer id) throws Exception {
		MemberComment comment = new MemberComment();
		comment.setId(id);
		if(id != null && "".equals(id)){
			if(commentMapper.isExist(comment)>0){
				commentMapper.del(id);
			}else{
				throw new Exception("该订单评价不存在");
			}
		}else{
			throw new Exception("参数错误");
		}
	}

}
