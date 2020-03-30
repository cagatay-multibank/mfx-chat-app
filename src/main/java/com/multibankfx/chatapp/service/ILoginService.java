package com.multibankfx.chatapp.service;

import com.multibankfx.chatapp.data.collection.UserSecurity;
import com.multibankfx.chatapp.data.collection.UserSession;

public interface ILoginService {

    boolean checkPasswords(String password1, String password2);

    void clearUserSecurityInformations(String userId);

    void clearUserSessions(String userId);

    UserSession createUserSession(String userId, String sid);

    UserSecurity createUserSecurity(String userId, String password);

    boolean checkPasswordByUserId(String userId, String password);
}
