package bmatser.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.MemberFavoriteMapper;
import bmatser.model.MemberFavorite;
import bmatser.service.MemberFavoriteI;
import bmatser.util.LoginInfo;

/**
 * 会员收藏
 * @author felx
 * @date 2016.02.17 
 */
@Service("memberFavorite")
public class MemberFavoriteImpl implements MemberFavoriteI {
	
	@Autowired
	private MemberFavoriteMapper memberFavoriteDao;
	/**
	 * 新增会员收藏
	 * @param LoginInfo loginInfo,MemberFavorite memberFavorite
	 * @return 
	 */
	@Override
	public int insert(LoginInfo loginInfo,MemberFavorite memberFavorite) {
		memberFavorite.setDealerId(Integer.parseInt(loginInfo.getDealerId()));
		return memberFavoriteDao.insert(memberFavorite);
	}
	/**
	 * 修改会员收藏
	 * @param String id,LoginInfo loginInfo,MemberFavorite memberFavorite
	 * @return 
	 */
	@Override
	public int updateById(String id,LoginInfo loginInfo,MemberFavorite memberFavorite) {
		if(StringUtils.isNotBlank(id)){
			memberFavorite.setId(Integer.parseInt(id));
		}
		if(null==memberFavorite.getDealerId()){
			memberFavorite.setDealerId(Integer.parseInt(loginInfo.getDealerId()));
		}
		return memberFavoriteDao.updateById(memberFavorite);
	}
	/**
	 * 删除会员收藏
	 * @param Integer id
	 * @return 
	 */
	@Override
	public int deleteById(String sellerGoodsIds,Integer dealerId) {
		int i=0;
		if(StringUtils.isNotBlank(sellerGoodsIds)){
			String[] strArray = null;  
	        strArray = sellerGoodsIds.split(",");
	        for(int j = 0; j < strArray.length; j++){
	        	i = memberFavoriteDao.deleteById(strArray[j],dealerId);
	        	i++;
	        }
		}
		return i;
	}
	/**
	 * 查询会员收藏关联信息
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
		if(StringUtils.isNotBlank(request.getParameter("buyNo"))){
			mp.put("buyNo", request.getParameter("buyNo").toString());			
		}
		if(StringUtils.isNotBlank(request.getParameter("favDate"))){
			mp.put("favDate", request.getParameter("favDate").toString());			
		}
		return memberFavoriteDao.selectById(mp);
	}
	/**
	 * 查询会员收藏总记录数
	 */
	public int count(Map mp){
		return memberFavoriteDao.count(mp);
	}
	/*public int count(){
		return memberFavoriteDao.count();
	}*/
	/**
	 * 查询会员是否被收藏
	 * @param HttpServletRequest request
	 * @return 
	 */
	@Override
	public int getNumber(Long sellerGoodsId, String dealerId) {
		return memberFavoriteDao.getNumber(sellerGoodsId, dealerId);
	}
	
	/**
	 * APP商城新增会员收藏
	 */
	@Override
	public int insertMemberFavorite(MemberFavorite memberFavorite) {
		return memberFavoriteDao.insert(memberFavorite);
	}
}
