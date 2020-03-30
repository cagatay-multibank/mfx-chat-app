package com.multibankfx.chatapp.data.repository;

import com.multibankfx.chatapp.data.collection.UserSecurity;
import com.multibankfx.chatapp.data.enumarated.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSecurityRepository extends MongoRepository<UserSecurity,String> {
    /**
     *
     * @param userId
     * @param status
     * @return
     */
    List<UserSecurity> findUserSecuritiesByUserIdAndStatus(String userId, Status status);


    /**
     *
     * @param userId
     * @param status
     */
    @Query(value = "update UserSecurity set sessionStatus=:sessionStatus where userId=:userId")
    void updateUserSecuritiesByUserId(@Param(value = "userId") String userId,@Param(value = "status") Status status);



}
