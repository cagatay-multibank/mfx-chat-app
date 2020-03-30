package com.multibankfx.chatapp.data.collection;

import com.multibankfx.chatapp.data.enumarated.ConversationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@Document
public class Conversation extends BaseCollection {

    private ConversationType conversationType;

    private String ownerUserId;

    private String user1Id;

    private String user2Id;


}
