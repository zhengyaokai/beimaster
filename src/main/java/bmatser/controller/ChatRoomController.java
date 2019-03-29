package bmatser.controller;

import com.wordnik.swagger.annotations.ApiOperation;
import bmatser.service.CorpusKeysServiceI;
import bmatser.util.ResponseMsg;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomController.class);

    /**
     * websocket的配置一定要放在 spring.xml中，别的地方将导致template无法注入
     */
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private CorpusKeysServiceI cks;

    private final static String gdb_auto_sister = "工电宝小助手";

    private final static String gdb_chatusers_update = "UPDATE_GDB_CHATUSERS_LIST_67885";

    //保存聊天室所有用户的信息
    private final static ConcurrentHashMap<String,String> chatUsersMap = new ConcurrentHashMap<String,String>();


    //广播推送
    @ApiOperation(value = "聊天室群聊")
    @MessageMapping("/handle1")//@MessageMapping接收客户端消息
    //@SendTo("/topic/talking")//@SendTo广播消息出去
    public void greetingBroadcast(@RequestBody ChatUserInfo topicMsg) throws InterruptedException {
        try{
            topicMsg = getUserDefinedName(topicMsg);
            if(StringUtils.isNotBlank(topicMsg.getContent())) {
                LOGGER.info("(广播推送)" + topicMsg.getContent());
                template.convertAndSend("/topic/talking", topicMsg);
                autoReplyByKeyword("/topic/talking", topicMsg);
            }
        }catch (Exception ex){
            LOGGER.error("(广播推送)失败:"+ex.getMessage());
        }

    }

    @ApiOperation(value = "聊天室私聊")
    @MessageMapping("/handle2")
    //@SendToUser("/ptp/talking")只能回复消息发送者，不能指定
    public void p2pHandler(@RequestBody ChatUserInfo ptpMsg) {
        try{
            ptpMsg = getUserDefinedName(ptpMsg);
            if(StringUtils.isNotBlank(ptpMsg.getContent())){
                LOGGER.info("(精准推送)"+ptpMsg.getContent());
                //消息发送给 接收者
                template.convertAndSend("/ptp/talking/"+ptpMsg.getReceivedUserId(),ptpMsg);
                //消息发给 发送者(也可以前端直接显示)
                template.convertAndSend("/ptp/talking/"+ptpMsg.getSendUserId(),ptpMsg);
            }
        }catch (Exception ex){
            LOGGER.error("(精准推送)失败:"+ex.getMessage());
        }
    }


    @ApiOperation(value = "聊天室用户添加或剔除")
    @RequestMapping(value="/gdb_chat_websocket/manager")
    @ResponseBody
    public List<ChatUserInfo> chatRoomUserManager(@RequestBody ChatUserInfo userManager){
        try {
            if(StringUtils.isBlank(userManager.getSendUserId()) || StringUtils.isBlank(userManager.getSendUserName()) ){
                return null;
            }
            if(userManager.getUserOper() == 0){//查询用户列表
                List<ChatUserInfo> list = new ArrayList<ChatUserInfo>();
                Iterator iter = chatUsersMap.entrySet().iterator(); // 获得map的Iterator
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    list.add(new ChatUserInfo(String.valueOf(entry.getKey()),String.valueOf(entry.getValue())));
                }
                return list;
            }else{
                if( userManager.getUserOper()==1 && !chatUsersMap.containsKey(userManager.getSendUserId()) ){
                    chatUsersMap.put(userManager.getSendUserId(),userManager.getSendUserName());
                }else if( userManager.getUserOper()==-1 && chatUsersMap.containsKey(userManager.getSendUserId()) ){
                    chatUsersMap.remove(userManager.getSendUserId());
                }
                //发送群聊，通知更新用户列表
                userManager.setReceivedUserId(null);
                userManager.setContent(gdb_chatusers_update);
                template.convertAndSend("/topic/talking", userManager);
            }
        } catch (Exception e) {
            LOGGER.error("聊天室用户操作失败!"+e.getMessage());
        }
        return Collections.emptyList();
    }

    @ApiOperation(value = "修改用户昵称")
    @RequestMapping(value="/gdb_chat_websocket/rename")
    @ResponseBody
    public ResponseMsg chatRoomRenameUser(@RequestBody ChatUserInfo userManager){
        ResponseMsg rMsg = new ResponseMsg();
        rMsg.setCode(-1);
        try {
            if(StringUtils.isBlank(userManager.getSendUserId()) || StringUtils.isBlank(userManager.getSendUserName()) ){
                return null;
            }
           if(chatUsersMap.containsKey(userManager.getSendUserId()) ){//改名
               chatUsersMap.put(userManager.getSendUserId(),userManager.getSendUserName());
               rMsg.setCode(1);
               userManager.setReceivedUserId(null);
               userManager.setContent(gdb_chatusers_update);
               template.convertAndSend("/topic/talking", userManager);
               LOGGER.info("ID为["+userManager.getSendUserId()+"]的用户成功改名为:"+userManager.getSendUserName());
            }
        } catch (Exception e) {
            rMsg.setError(e);
            LOGGER.error("聊天室用户操作失败!"+e.getMessage());
        }
        return rMsg;
    }


    //返回
    private ChatUserInfo getUserDefinedName(ChatUserInfo cuInfo){
        if(chatUsersMap.containsKey(cuInfo.getSendUserId()) && !cuInfo.getSendUserName().equals(chatUsersMap.get(cuInfo.getSendUserId()))){//改名
            cuInfo.setSendUserName(chatUsersMap.get(cuInfo.getSendUserId()));
        }
        return cuInfo;
    }

    static class ChatUserInfo{
        private String sendUserName;
        private String sendUserId;
        private String receivedUserId;
        private String content;

        public ChatUserInfo() {
        }
        public ChatUserInfo(String sendUserId, String sendUserName) {
            this.sendUserName = sendUserName;
            this.sendUserId = sendUserId;
        }
        private int userOper;//聊天室新增用户或删除用户标识 1 新增; -1减少
        public String getSendUserName() {
            return sendUserName;
        }
        public void setSendUserName(String sendUserName) {
            this.sendUserName = sendUserName;
        }
        public String getSendUserId() {
            return sendUserId;
        }
        public void setSendUserId(String sendUserId) {
            this.sendUserId = sendUserId;
        }
        public String getReceivedUserId() {
            return receivedUserId;
        }
        public void setReceivedUserId(String receivedUserId) {
            this.receivedUserId = receivedUserId;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public int getUserOper() {
            return userOper;
        }
        public void setUserOper(int userOper) {
            this.userOper = userOper;
        }
    }

    /**
     * 自动回复小助手
     * @param OriginContent
     * @return
     */
    public void autoReplyByKeyword(String destination,ChatUserInfo OriginContent) {
        try{
            String gdbQAs = cks.getQAs(OriginContent.getContent());
            if(StringUtils.isNotBlank(gdbQAs)){
                OriginContent.setSendUserName(gdb_auto_sister);
                OriginContent.setContent(gdbQAs);
                template.convertAndSend(destination,OriginContent);
            }
        }catch (Exception e){
            LOGGER.error("工电宝智能助手回复失败!"+e.getMessage());
        }
    }

}
