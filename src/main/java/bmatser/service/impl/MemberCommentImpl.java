package bmatser.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.MemberCommentMapper;
import bmatser.model.MemberComment;
import bmatser.service.MemberCommentI;
@Service("MemberComment")
public class MemberCommentImpl implements MemberCommentI{
	@Autowired
	private MemberCommentMapper memberCommentDao;
	@Override
	public void insertMemberComment(MemberComment memberComment) {
		//插入评价表
		memberCommentDao.insertMemberComment(memberComment);
		//更新order_info表is_comment
		memberCommentDao.updateIsComment(memberComment);
		
	}
	@Override
	public Map selectMemberComment(MemberComment memberComment) {
		//查看订单评价
		return memberCommentDao.selectMemberComment(memberComment);
	}

}
