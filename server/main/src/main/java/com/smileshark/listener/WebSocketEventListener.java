package com.smileshark.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
//        System.out.println("STOMP 连接建立: " + event.getMessage());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        System.out.println("STOMP 断开连接: " + event.getMessage());
//        String customer = (String) headerAccessor.getSessionAttributes().get("customer")
    }
}
