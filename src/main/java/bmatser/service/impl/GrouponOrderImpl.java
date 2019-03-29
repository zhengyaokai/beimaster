package bmatser.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.GrouponOrderMapper;
import bmatser.service.GrouponOrderI;
/**
 * 
 * 团购商品 
 *
 */
@Service("grouponOrder")
public class GrouponOrderImpl implements GrouponOrderI {
	@Autowired
	private GrouponOrderMapper grouponOrderDao;
	/**
	 * 
	 * 团购商品列表 
	 * 个人信息中 我的团购修改为和团购活动一样的顺序
	 *  
	 */
	@Override
	public List selectGrouponGoods(Integer dealerId,HttpServletRequest request,Integer page,Integer rows,Map mp) throws Exception {
		String allActivityParam=request.getParameter("allActivityParam");
		String grouponNameParam=URLDecoder.decode(null==request.getParameter("grouponName")?"":request.getParameter("grouponName").toString(),"utf-8");
		/*//团购状态0:全部 1:未进行 2：进行中 3:已开团 4:已结束 5:取消
		if(StringUtils.isNotBlank(allActivityParam)){
		  if(allActivityParam.equals("0")){
			  allActivityParam="";
		  }else if(allActivityParam.equals("3")){
			  allActivityParam=" AND (ga.groupon_amount+0) >=(ga.groupon_open_condition+0) and  ga.end_time>=NOW() ";
		  }else if(allActivityParam.equals("2")){
			  allActivityParam=" AND (ga.groupon_amount+0) < (ga.groupon_open_condition+0) and  ga.end_time>=NOW() ";
		  }else if(allActivityParam.equals("4")){
			  allActivityParam=" AND ga.end_time<NOW() ";
		  }
		}*/
		
		//团购状态0:全部 1:未进行 2：进行中 3:已开团 4:已结束 5:取消
				//20160711    新增       团购活动修改
				//上线时间≤当前时间＜活动开始时间；那么活动状态【即将开始】  
		        //开始时间≤当前时间＜活动结束时间；那么活动状态为【进行中】
		        //当前时间≥活动结束时间；那么活动状态为【已结束】
		if(StringUtils.isNotBlank(allActivityParam)){
		  if(allActivityParam.equals("0")){
			  allActivityParam="";
		  }else if(allActivityParam.equals("1")){//即将开始 
			  allActivityParam=" AND ga.start_time>NOW() AND ga.online_time<=NOW() ";
		 /* }else if(chooseActivityModule.equals("3")){
			  chooseActivityModule=" AND (ga.groupon_amount+0) >=(ga.groupon_open_condition+0) and  ga.end_time>=NOW() AND ga.start_time <= NOW() ";*/
		  }else if(allActivityParam.equals("2")){//进行中  
			  allActivityParam=" and  ga.end_time>NOW() AND ga.start_time <= NOW() ";
		  }else if(allActivityParam.equals("4")){
			  allActivityParam=" AND ga.end_time<=NOW() ";
		  }
		}
		mp.put("dealerId", dealerId);
		mp.put("allActivityParam", allActivityParam);
		mp.put("grouponName", grouponNameParam);
		return  grouponOrderDao.selectGrouponGoods(dealerId,allActivityParam,grouponNameParam,page,rows);
	}
	
	@Override
	public int count(Map mp) {
		return grouponOrderDao.count(mp);
	}

	/**
	 * 
	 * 活动汇 团购活动列表  
	 *
	 */
	@Override
	public List selectGrouponActivitySink(HttpServletRequest request,Integer page, Integer rows, Map mp) {
		String chooseActivityModule=request.getParameter("type");
		//团购状态0:全部 1:未进行 2：进行中 3:已开团 4:已结束 5:取消
		//20160711    新增       团购活动修改
		//上线时间≤当前时间＜活动开始时间；那么活动状态【即将开始】  
        //开始时间≤当前时间＜活动结束时间；那么活动状态为【进行中】
        //当前时间≥活动结束时间；那么活动状态为【已结束】

				if(StringUtils.isNotBlank(chooseActivityModule)){
				  if(chooseActivityModule.equals("0")){
						 chooseActivityModule="";
				  }else if(chooseActivityModule.equals("1")){//即将开始 
					  chooseActivityModule=" AND ga.start_time>NOW() AND ga.online_time<=NOW() ";
				 /* }else if(chooseActivityModule.equals("3")){
					  chooseActivityModule=" AND (ga.groupon_amount+0) >=(ga.groupon_open_condition+0) and  ga.end_time>=NOW() AND ga.start_time <= NOW() ";*/
				  }else if(chooseActivityModule.equals("2")){//进行中  
					  chooseActivityModule=" and  ga.end_time>NOW() AND ga.start_time <= NOW() ";
				  }else if(chooseActivityModule.equals("4")){
					  chooseActivityModule=" AND ga.end_time<=NOW() ";
				  }
				}
				mp.put("chooseActivityModule", chooseActivityModule);
				return grouponOrderDao.selectGrouponActivitySink(chooseActivityModule,page,rows,null==mp.get("dealerId")?"":mp.get("dealerId").toString());
	}
	
	@Override
	public int countActivitySink(Map mp) {
		return grouponOrderDao.countActivitySink(mp);
	}
	/**
	 * 
	 * 活动汇 单个团购活动详情 
	 *
	 */
	@Override
	public Map selectGrouponActivityDetail(HttpServletRequest request,String dealerId) {
		String id=request.getParameter("id");
		return grouponOrderDao.selectGrouponActivityDetail(id,dealerId);
	}
	/**
	 * 
	 * 活动汇 团购详情列表矩阵 
	 *
	 */
	@Override
	public List selectGrouponActivityDetailList(String id,String dealerId,Integer page,Integer rows) {
		if(StringUtils.isBlank(id)){
			throw new RuntimeException("参数错误!");
		}
		return grouponOrderDao.selectGrouponActivityDetailList(id,dealerId,page,rows);
	}

	@Override
	public int countActivityDetailList(Map mp) {
		return grouponOrderDao.countActivityDetailList(mp);
	}
}
