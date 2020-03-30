package com.multibankfx.chatapp.controller.socket;

import com.multibankfx.chatapp.service.IMessageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MessageSocketController {

    @Autowired
    private IMessageService messageService;

    @MessageMapping("/send/message")
    public void sendMessage(SendMessageRequest sendMessageRequest) {
    }
    @MessageMapping("/receive/message")
    public void receiveMessage() {

    }

    @Getter
    @Setter
    private class SendMessageRequest {

        private String receiverUserId;

        private String content;

        private String conversationId;

    }
}
