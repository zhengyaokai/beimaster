package bmatser.service;

import java.util.Map;

import bmatser.model.MemberComment;

public interface MemberCommentI {

	void insertMemberComment(MemberComment memberComment);

	Map selectMemberComment(MemberComment memberComment);

}
