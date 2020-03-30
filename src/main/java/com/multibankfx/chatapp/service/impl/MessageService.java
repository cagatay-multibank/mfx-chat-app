package com.multibankfx.chatapp.service.impl;

import com.multibankfx.chatapp.data.collection.Message;
import com.multibankfx.chatapp.data.repository.ConversationRepository;
import com.multibankfx.chatapp.data.repository.MessageRepository;
import com.multibankfx.chatapp.service.IMessageService;
import com.multibankfx.chatapp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class MessageService implements IMessageService {

    @Autowired
    private IUserService userService;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;


    @Override
    public Message create(String senderUserId, String receiverUserId, String conversationId, String content) {
        Message message = new Message();
        message.setContent(content);
        message.setSentTime(System.currentTimeMillis());
        message.setSenderUserId(senderUserId);
        message.setReceiverUserId(receiverUserId);
        message.setConversationId(conversationId);
        message = messageRepository.save(message);
        return message;
    }
}
