package com.multibankfx.chatapp.data.repository;

import com.multibankfx.chatapp.data.collection.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message,String> {
}
