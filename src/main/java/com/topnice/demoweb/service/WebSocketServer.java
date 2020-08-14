package com.topnice.demoweb.service;


import com.alibaba.fastjson.JSON;
import com.topnice.demoweb.entity.Enterprise;
import com.topnice.demoweb.entity.Hosts;
import com.topnice.demoweb.util.EnterUtil;
import com.topnice.demoweb.util.WebSocketUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/imserver/{enterId}/{linkId}/{name}")
@Component
public class WebSocketServer {
    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
//    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * concurrent包的线程安全Set，这个用来存放每个企业的ConcurrentHashMap数据。
     */
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketServer>> enterMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收enterId
     */
    private String enterId = "";
    /**
     * 用户名称
     **/
    /**
     * 接收linkId
     */
    private String linkId = "";
    /**
     * 用户名称
     **/
    private String name;

    private String hostId;

    //IHostsService hostsService = (IHostsService)MyApplicationContextAware.getApplicationContext().getBean("IHostsService");


    private  static HostsService hostsService;

    private  static EnterpriseService enterpriseService;

    @Autowired
    public void setUserService(HostsService hostsService) {
        WebSocketServer.hostsService = hostsService;
    }

    @Autowired
    public void setEnterService(EnterpriseService enterpriseService) {
        WebSocketServer.enterpriseService = enterpriseService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public synchronized void onOpen(Session session, @PathParam("enterId") String enterId, @PathParam("linkId") String linkId, @PathParam("name") String name) {
        if (name == null) {
            name = "未知主机";
        }
//        System.out.println("获取到地址" + WebSocketUtil.getRemoteAddress(session));
//        System.out.println(enterId + "#######" + linkId + "##########" + name);
        try {
            this.name = URLDecoder.decode(name, "utf-8");
            name = this.name;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.session = session;
        this.linkId = linkId;
        this.enterId = enterId;
        this.hostId=WebSocketUtil.getRemoteAddress(session)+"";
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
        //先判断企业是否存在
        Enterprise enterprise=enterpriseService.findByEnterId(enterId);
        if (enterprise==null){
            sendMessage("{type:'-1',data:''}");
            return;
        }

            //判断主机是否已保存
         Hosts hosts=hostsService.findEnterIdAndHostLinkId(enterId,linkId);
        if (hosts==null){
            //判断是否还可以添加主机
            //获取企业主机数量
            String hostNum = EnterUtil.decrypt(enterprise.getHostNumAuth());
            //查询当前企业添加的主机数量
            int num = hostsService.findEnterHostNum(enterId);
            int enterHostNum = Integer.parseInt(hostNum);
            if (num <= enterHostNum) {
                //不超出，添加主机
                hostsService.add(hostId, linkId, name, enterId);
            } else {
                sendMessage("{type:'-2',data:''}");
                return;
            }

        }else {
            updateHostState(enterId,linkId,"0");
        }

        //链接操作
        //判断是否存在企业主机列表
        ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
        if (enterMap.containsKey(enterId)) {
            //获取企业下所有主机
            ConcurrentHashMap<String, WebSocketServer> linkMap = enterMap.get(enterId);
            //存在，删除在添加
            if (linkMap.containsKey(linkId)) {
                linkMap.remove(linkId);
                linkMap.put(linkId, this);
            }
            //不存在，添加
            else {
                linkMap.put(linkId, this);
                addOnlineCount();
            }
        } else {
            //保存企业主机连接
            webSocketMap.put(linkId, this);
            enterMap.put(enterId, webSocketMap);
            addOnlineCount();
        }

        System.out.println("当前用户id：" + linkId);
        //判断该链接是否存在
//        Hosts hosts = hostsService.selectHostId(userId);
//        if (hosts != null) {
//            hosts.setLinkState("0");
//            hostsService.updateHostLState(hosts);
//        } else {
//            hostsService.addHosts(userId, name);
//        }
        log.info("用户连接:" + name + linkId + ",当前在线人数为:" + getOnlineCount());
        // System.out.println("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());
         sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:" + linkId + ",网络异常!!!!!!");
            //System.out.println("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (enterMap.containsKey(enterId)) {
            if (enterMap.get(enterId).containsKey(linkId)) {
                enterMap.get(enterId).remove(linkId);
                //查询主机，修改状态
                updateHostState(enterId,linkId,"1");
                subOnlineCount();
            }
        }
        log.info("用户退出:" + linkId + ",当前在线人数为:" + getOnlineCount());
        //System.out.println("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }



    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + linkId + ",报文:" + message);
        //System.out.println("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
//        if(StringUtils.isNotBlank(message)){
//            try {
//                //解析发送的报文
//                JSONObject jsonObject = JSON.parseObject(message);
//                //追加发送人(防止串改)
//                jsonObject.put("fromUserId",this.userId);
//                String toUserId=jsonObject.getString("toUserId");
//                //传送给对应toUserId用户的websocket
//                if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
//                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
//                }else{
//                    log.error("请求的userId:"+toUserId+"不在该服务器上");
//                   // System.out.println("请求的userId:"+toUserId+"不在该服务器上");
//                    //否则不在这个服务器上，发送到mysql或者redis
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }

    public void updateHostState(String enterId,String linkId,String linkState){
            Hosts hosts = hostsService.findEnterIdAndHostLinkId(enterId,linkId);
            if (hosts != null) {
                hosts.setLinkState(linkState);
                hosts.setLinkTime(new Date());
                hostsService.modifyHostLState(hosts);
            }

    }

    /**
     * 用户链接错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.linkId + ",原因:" + error.getMessage());
        updateHostState(enterId,linkId,"1");
       // error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        log.info("推送消息到窗口" + linkId + "，推送内容:" + message);
        this.session.getBasicRemote().sendText(message);
    }

//    public static void selectAllUser() {
//        Set<String> keys = webSocketMap.keySet();
//        for (String a : keys) {
//            System.out.println("当前连接id：###########" + a);
//        }
//    }
    //对某企业下所有主机发送消息
    public static void senEnterAllMessage(String enterId, String message) throws IOException {
        //获取所有key
        Set<String> keys = enterMap.get(enterId).keySet();
        Iterator<String> iterator1 = keys.iterator();
        while (iterator1.hasNext()) {
            // System.out.print(iterator1.next() +", ");
            Map<String, String> map = new HashMap<>();
            map.put("type", "1");
            map.put("data", message);
            enterMap.get(enterId).get(iterator1.next()).sendMessage(JSON.toJSONString(map));
        }
        //获取所有value
//        Collection<Integer> values=map.values();
//        Iterator<Integer> iterator2=values.iterator();
//        while (iterator2.hasNext()){
//            System.out.print(iterator2.next()+", ");
//        }

    }

    /**
     * @desc: 查询某企业下所有主机
     * @author: sen
     * @date: 2020/8/6 0006 7:50
     **/
    public static String enterHostAll(String enterId) throws IOException {
        Set<String> keys = enterMap.get(enterId).keySet();
        Iterator<String> iterator1 = keys.iterator();
        String sumHost = "";
        while (iterator1.hasNext()) {
            sumHost = sumHost + "######" + iterator1.next();
        }
        return sumHost;
    }

    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, String enterId, @PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && enterMap.get(enterId).containsKey(userId)) {
            Map<String, String> map = new HashMap<>();
            map.put("type", "1");
            map.put("data", message);
            enterMap.get(enterId).get(userId).sendMessage(JSON.toJSONString(map));
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    /**
     * 群发自定义消息
     */
    /*public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("推送消息到窗口"+userId+"，推送内容:"+message);
        for (ImController item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(userId==null) {
                    item.sendMessage(message);
                }else if(item.userId.equals(userId)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }*/
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}