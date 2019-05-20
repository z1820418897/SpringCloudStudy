package com.gc.webserver.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;

@Component
public class SocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);

    private static final ArrayList<WebSocketSession> users;
    static{
        users=new ArrayList<>();
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.info("系统已经建立socket连接");
        log.info(webSocketSession.getAttributes().get("WEBSOCKET_USERID").toString());
        users.add(webSocketSession);

    }

    /**
     * 给指定用户发送信息
     * */

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

        Long userId = (Long) webSocketSession.getAttributes().get("WEBSOCKET_USERID");
        String message;
        log.info("处理推送的消息");
        //判断客户端是否消息发送，不需要客户端与客户端的单向通信，此处可省略。

        if (!webSocketMessage.getPayload().equals("undefined")){
            message = "客户端发送的消息为：" + webSocketMessage.getPayload();
        }else {
            message = "推送测试信息 ---" + System.currentTimeMillis();
        }

        sendMessageToUser(userId, new TextMessage(message));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }
        log.error("系统WebSocket传输错误，连接关闭！用户ID：" + webSocketSession.getAttributes().get("WEBSOCKET_USERID"), throwable);
        //移除异常用户信息
        users.remove(webSocketSession);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
    }
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }





    public void sendMessageToUser(Long userId, TextMessage message) {
        log.info("发送消息至用户！");
        for (WebSocketSession user : users) {
            if (user.getAttributes().get("WEBSOCKET_USERID").equals(userId)) {
                sendSocketSessionMsg(user, message);
            }
        }
    }

    /**
     * 发送消息
     * @param user 接收用户
     * @param message 消息
     */
    private boolean sendSocketSessionMsg(WebSocketSession user, TextMessage message) {
        String msg = message.getPayload();
        boolean sendSuccess = true;
        try {
            if (user.isOpen()) {
                synchronized (user) {
                    user.sendMessage(message);
                }
            } else {
                log.error("WebSocket连接未打开，系统消息推送失败：" + msg);
                sendSuccess = false;
            }
        } catch (Exception e) {
            log.error("系统消息推送失败：" + msg, e);
            sendSuccess = false;
        }
        return sendSuccess;
    }
}
