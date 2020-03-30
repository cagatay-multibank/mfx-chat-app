package com.multibankfx.chatapp.data.repository;

import com.multibankfx.chatapp.data.collection.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation,String> {

    /**
     *
     * @param user1Id
     * @param user2Id
     * @return
     */
    @Query(value = "select c from Conversation c where  (c.user2Id=:user1Id and c.user1Id=:user2Id) or (c.user1Id=:user1Id and c.user2Id=:user2Id) ")
    List<Conversation> fetchConversations(@Param(value = "user1Id") String user1Id, @Param(value = "user2Id") String user2Id);
}
