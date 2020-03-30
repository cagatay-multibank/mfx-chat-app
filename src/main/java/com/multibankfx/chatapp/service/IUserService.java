package com.multibankfx.chatapp.service;

import com.multibankfx.chatapp.data.collection.User;

public interface IUserService {
    User createUser(String username, String name, String surname);

    User findByUsername(String username);
}
