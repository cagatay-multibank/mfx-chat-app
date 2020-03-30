package com.multibankfx.chatapp.service.impl;

import com.multibankfx.chatapp.data.repository.ConversationRepository;
import com.multibankfx.chatapp.service.IConversationService;
import com.multibankfx.chatapp.data.collection.Conversation;
import com.multibankfx.chatapp.data.enumarated.ConversationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ConversationService implements IConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Conversation findOrCreate(String ownerUserId, String receiverUserId) {
        List<Conversation> conversations = conversationRepository.fetchConversations(ownerUserId, receiverUserId);
        return CollectionUtils.isEmpty(conversations) ? conversations.get(0) : create(ownerUserId,receiverUserId);
    }

    @Override
    public Conversation create(String ownerUserId, String receiverUserId) {

        if(StringUtils.isEmpty(ownerUserId)) {
            // todo throw exception
            return null;
        } else if(StringUtils.isEmpty(receiverUserId)) {
            // todo throw exception
            return null;
        }
        Conversation conversation = new Conversation();
        conversation.setOwnerUserId(ownerUserId);
        conversation.setUser1Id(ownerUserId);
        conversation.setUser2Id(receiverUserId);
        conversation.setConversationType(ConversationType.NORMAL);
        conversation.setCreatedTime(System.currentTimeMillis());
        conversation.setModifiedTime(System.currentTimeMillis());

        return conversationRepository.save(conversation);
    }
}
