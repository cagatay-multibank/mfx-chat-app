package com.multibankfx.chatapp.data.collection;

import com.multibankfx.chatapp.data.enumarated.MessageStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@Document(collection = "message")
public class Message extends BaseCollection {

    private String content;

    private Long sentTime;

    private Long receivedTime;

    private String senderUserId;

    private String receiverUserId;

    private String conversationId;

    private MessageStatus messageStatus;

}
