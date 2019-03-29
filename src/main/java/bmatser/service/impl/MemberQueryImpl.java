package bmatser.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.MemberQueryMapper;
import bmatser.model.MemberQuery;
import bmatser.service.MemberQueryI;
import bmatser.util.LoginInfo;

/**
 * 会员咨询
 * @author felx
 * @date 2016.02.18 
 */
@Service("memberQuery")
public class MemberQueryImpl implements MemberQueryI {
	
	@Autowired
	private MemberQueryMapper memberQueryMapper;
	/**
	 * 新增会员咨询
	 * @param MemberQuery memberQuery  sellerGoodsId&queryContent
	 * @return 
	 */
	@Override
	public int insert(MemberQuery memberQuery) {
		return memberQueryMapper.insert(memberQuery);
	}
	
	/**
	 * 查询会员咨询关联信息
	 * @param HttpServletRequest request,Map mp
	 * @return 
	 */
	@Override
	public List selectById(HttpServletRequest request,Map mp) {
		if(StringUtils.isNotBlank(request.getParameter("goodsName"))){
			try {
				mp.put("goodsName", URLDecoder.decode(request.getParameter("goodsName"),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(request.getParameter("sellerGoodsId"))){
			mp.put("sellerGoodsId", Integer.parseInt(request.getParameter("sellerGoodsId")));
		}
		return memberQueryMapper.selectById(mp);
	}
	/**
	 * 查询会员咨询总记录数
	 * @param HttpServletRequest request,Map mp
	 * @return 
	 */
	@Override
	public Long count(Map mapPut) {
		return memberQueryMapper.count(mapPut);
	}
	
}
