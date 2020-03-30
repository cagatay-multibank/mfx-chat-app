package com.multibankfx.chatapp.data.wrapper;

import com.multibankfx.chatapp.data.collection.Message;
import com.multibankfx.chatapp.data.dto.MessageInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MessageInfoWrapper {
    public MessageInfoDto get(Message message) {
        MessageInfoDto dto = new MessageInfoDto();
        dto.setId(message.getId());
        dto.setConversationId(message.getConversationId());
        dto.setContent(message.getContent());
        return dto;
    }
}
