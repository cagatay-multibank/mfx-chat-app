package com.multibankfx.chatapp.service;

import com.multibankfx.chatapp.data.collection.Conversation;

public interface IConversationService {
    Conversation findOrCreate(String ownerUserId, String receiverUserId);

    Conversation create(String ownerUserId, String receiverUserId);
}
