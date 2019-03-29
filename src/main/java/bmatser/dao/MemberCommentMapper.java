package bmatser.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.MemberComment;

public interface MemberCommentMapper {
	void insertMemberComment(MemberComment memberComment);

	void updateIsComment(MemberComment memberComment);

	Map selectMemberComment(MemberComment memberComment);

}
