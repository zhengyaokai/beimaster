package bmatser.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import bmatser.dao.SysMessageMapper;
import bmatser.service.SysMessageI;

@Service("messageService")
public class SysMessageImpl implements SysMessageI {

	@Autowired
	private SysMessageMapper messageDao;

    @Override
    public List<Map<String, Object>> getSysNotice(Integer page, Integer rows) throws Exception {

       return messageDao.getSysNotice(page,rows);
    }

    @Override
    public List<Map<String, Object>> getSysNoticeById(Integer id) {


        return messageDao.getSysNoticeById(id);
    }

    /**
	 * 获取数据更新动态
	 * @return
	 * 2017-9-7
	 *
	 */
	@Override
	public List findDataMessages() {
		return messageDao.findDataMessages();
	}

	@Override
	public List findHotKeyWord() {
		String s = messageDao.findHotKeyWord();
		List<String> keyword = null;
		if(StringUtils.isNoneBlank(s)){
			keyword = new ArrayList<String>();
			String[] key = s.split(",");
			for (int i = 0; i < key.length; i++) {
				keyword.add(key[i]);
			}
		}
		return keyword;
	}

	@Override
	public List findDataNotice() {
		return messageDao.findDataNotice();
	}


    @Override
    public List<Map<String,Object>> findDataNoticeForIos() {
	    List<Map<String,Object>> list = new ArrayList<>();
        List<Map<String,Object>> resultList = new ArrayList<>();
         list = messageDao.findDataNoticeForIos();
         for(int i=0;i<list.size()-1;i+=2){
             Map<String,Object> map = new HashMap<>();
                map.put("first",list.get(i));
                map.put("second",list.get(i+1));
                resultList.add(map);
         }
        return resultList ;
    }


	/**
	 * 查询通告详情 
	 * @return title标题，content内容 ，create_time发布时间 ，message_type消息类型（1：数据更新 2：公告 3:移动端消息推送）
	 */
	@Override
	public List selectSysMessage(HttpServletRequest request) {
		Integer id=null;
				if(StringUtils.isNotBlank(request.getParameter("id"))){
					id=Integer.parseInt(request.getParameter("id").toString());
				}
		return messageDao.selectSysMessage(id);
	}
	
	/**
	 * saas贷通信息列表
	 * @param page
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map getSysMessageList(Integer dealerId, int page, int rows) {
		Map m = new HashMap();	
		//查询用户贷通信息列表
		List list = messageDao.getSysMessageList(dealerId,page,rows);
		int count = messageDao.getAllSysMessageListCount(dealerId);//消息总数		
		m.put("list", list);
		m.put("count", count);
		return m;
	}
	
	/**
	 * saas贷通未读信息总数	
	 */
	@Override
	public int getUnreadSysMessageCount(Integer dealerId) {
		int unreadCount = messageDao.getSysMessageListCount(dealerId);//未读消息总数
		return unreadCount;
	}
	/**
	 * saas贷通信息列表批量删除
	 * @return
	 */
	@Override
	public void deleteSysMessageById(String ids) throws Exception {
		if(StringUtils.isBlank(ids)){
			throw new Exception("参数错误!");
		}
		String[] idsArr = ids.split(",");
		for(int i=0;i<idsArr.length;i++){
			messageDao.deleteSysMessageById(idsArr[i]);
		}
	}
	
	/**
	 * saas贷通信息列表批量更新已读
	 * @return
	 */
	@Override
	public void updateIsRead(Integer dealerId, String ids) throws Exception {
		if(StringUtils.isBlank(ids)){
			throw new Exception("参数错误!");
		}
		String[] idsArr = ids.split(",");
		for(int i=0;i<idsArr.length;i++){
			messageDao.updateIsRead(idsArr[i],dealerId);
		}
	}
	
	/**
	 * saas贷通信息点击查看
	 * @return
	 */
	@Override
	public Map getSysMessageInfo(Integer dealerId, String id) throws Exception {
		Map map = new HashMap();
		if(StringUtils.isBlank(id)){
			throw new Exception("参数错误!");
		}
		map = messageDao.getSysMessageInfo(dealerId,id);//查询消息内容
		if(map !=null && !map.isEmpty()){
			if("false".equals(map.get("isRead").toString())){
				messageDao.updateIsRead(id,dealerId);//如果未读 则更新为已读
				map.put("isRead", true);
			}
		}
		return map;
	}

    @Override
    public List findDataNoticeDetails(int page, int rows) {

        return messageDao.findDataDetail(page,rows);
    }

    /**
     * create by yr on 2018-11-26 修改iOS质询
     */
    @Override
    public List<Map<String, Object>> findDataNoticeDetailsForIos(int page, int rows) {


        List<Map<String, Object>> list = messageDao.findDataDetailForIos(page,rows);;
//
        return list;
    }
}
