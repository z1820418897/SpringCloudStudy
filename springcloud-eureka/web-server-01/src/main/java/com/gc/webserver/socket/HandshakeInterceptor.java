package com.gc.webserver.socket;


import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@Component
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * 握手之前
     * */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        attributes.put("WEBSOCKET_USERID",1L);

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    /**
     * 握手之后
     * */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {



        super.afterHandshake(request, response, wsHandler, ex);
    }
}
