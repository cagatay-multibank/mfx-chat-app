package com.multibankfx.chatapp.facade;

import com.multibankfx.chatapp.service.IConversationService;
import com.multibankfx.chatapp.data.collection.Conversation;
import com.multibankfx.chatapp.data.collection.Message;
import com.multibankfx.chatapp.data.dto.MessageInfoDto;
import com.multibankfx.chatapp.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendMessageFacade {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IConversationService conversationService;


    public MessageInfoDto sendMessage(String senderUserId, String receiverUserId,String conversationId, String content) {
        MessageInfoDto messageInfoDto = new MessageInfoDto();

        if(StringUtils.isEmpty(conversationId)) {
            Conversation conversation = conversationService.create(senderUserId, receiverUserId);
            if(conversation != null) {
                conversationId = conversation.getId();
            }
        }

        Message message = messageService.create(senderUserId,receiverUserId,conversationId,content);

        return messageInfoDto;
    }



}
