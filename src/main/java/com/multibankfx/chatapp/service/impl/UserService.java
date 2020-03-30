package com.multibankfx.chatapp.service.impl;

import com.multibankfx.chatapp.data.collection.User;
import com.multibankfx.chatapp.data.repository.UserRepository;
import com.multibankfx.chatapp.data.enumarated.UserStatus;
import com.multibankfx.chatapp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Transactional
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(String username, String name, String surname) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        if(StringUtils.isEmpty(username)) {
            log.warn("username can not be empty");
            return null;
        }
        return userRepository.findUserByUsername(username);
    }
}
