package com.multibankfx.chatapp.service;

import com.multibankfx.chatapp.data.collection.Message;

public interface IMessageService {
    Message create(String senderUserId, String receiverUserId, String conversationId, String content);
}
