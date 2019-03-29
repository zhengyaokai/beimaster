package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.SysMessage;

public interface SysMessageMapper {
    
	List findDataMessages();

	String findHotKeyWord();
	
	List findDataNotice();

    /**
     * 获得详细消息分页
     */
    List findDataDetail(@Param("page")int page,@Param("rows") int rows);

    /**
     * ios获得资讯
     */
    List<Map<String,Object>> findDataDetailForIos(@Param("page")int page,@Param("rows") int rows);
    /**
     * Ios获得轮播消息
     * @return
     */
    List<Map<String,Object>> findDataNoticeForIos();

	//查询商城首页通知公告  add --20160603
    List findMallNotice();
	
	List selectSysMessage(Integer id);
	
	//查询用户贷通信息列表
	List getSysMessageList(@Param("dealerId") Integer dealerId,@Param("page") int page,@Param("rows") int rows);
	//消息总数
	int getAllSysMessageListCount(@Param("dealerId")Integer dealerId);
	//未读消息总数
	int getSysMessageListCount(@Param("dealerId")Integer dealerId);
	
	//saas贷通信息列表批量删除
	void deleteSysMessageById(@Param("id") String id);
	
	//saas贷通信息列表批量更新已读
	void updateIsRead(@Param("id") String id,@Param("dealerId") Integer dealerId);
	
	// saas贷通未读信息点击查看
	Map getSysMessageInfo(@Param("dealerId")Integer dealerId,@Param("id") String id);


    /**
     * 分页查询系统公告
     * @param page
     * @param rows
     * @return
     */
    List<Map<String,Object>> getSysNotice(@Param("page")Integer page,@Param("rows") Integer rows);

    List<Map<String,Object>> getSysNoticeById(@Param("id")Integer id);

}