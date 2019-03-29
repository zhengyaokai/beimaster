package bmatser.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import bmatser.model.MemberComment;

public interface GoodsCommentI {

	Map getGoodsCommentByPage(HttpServletRequest request, int page, int row) throws Exception;

	void addGoodsCommentByPage(MemberComment comment)  throws Exception ;

	void checkGoodsComment(Integer id, String dealerId)  throws Exception ;

	void delGoodsComment(Integer id)  throws Exception;

}
