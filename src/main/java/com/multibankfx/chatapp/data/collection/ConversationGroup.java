package com.multibankfx.chatapp.data.collection;

import org.springframework.data.annotation.Id;

public class ConversationGroup {

    @Id
    private String id;

    private String conversationId;

    private String userId;

}
