package com.multibankfx.chatapp.data.repository;

import com.multibankfx.chatapp.data.collection.UserSession;
import com.multibankfx.chatapp.data.enumarated.SessionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserSessionRepository extends MongoRepository<UserSession,String> {
    /**
     *
     * @param userId
     * @param sessionStatus
     * @return
     */
    List<UserSession> findUserSessionsByUserIdAndSessionStatus(String userId, SessionStatus sessionStatus);


}
