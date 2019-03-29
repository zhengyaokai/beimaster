package bmatser.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SysMessageI {

	List findDataMessages();

	List findHotKeyWord();

	List findDataNotice();

	List findDataNoticeDetails(int page ,int rows);

    /**
     * 为了ios返回资讯
     * @return
     */
    List<Map<String,Object>> findDataNoticeDetailsForIos (int page,int rows);

    List<Map<String,Object>> findDataNoticeForIos();
	
	List selectSysMessage(HttpServletRequest request);

	Map getSysMessageList(Integer dealerId, int page, int rows);

	void deleteSysMessageById(String ids) throws Exception;

	void updateIsRead(Integer dealerId, String ids) throws Exception;

	Map getSysMessageInfo(Integer dealerId, String id) throws Exception;

	int getUnreadSysMessageCount(Integer dealerId);

    /**
     * create by yr on 2018-11-05
     * 获得消息公告
     * */
	List<Map<String,Object>> getSysNotice(Integer page,Integer rows) throws Exception;

    List<Map<String,Object>> getSysNoticeById(Integer id);

}
