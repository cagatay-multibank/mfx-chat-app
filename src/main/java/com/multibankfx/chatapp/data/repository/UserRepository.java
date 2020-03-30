package com.multibankfx.chatapp.data.repository;

import com.multibankfx.chatapp.data.collection.User;
import com.multibankfx.chatapp.data.enumarated.UserStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    /**
     *
     * @param username
     * @return
     */
    public User findUserByUsername(String username);

    User findUserByUsernameAndUserStatus(String username, UserStatus userStatus);
}
