package com.multibankfx.chatapp.facade;

import com.multibankfx.chatapp.data.collection.User;
import com.multibankfx.chatapp.data.wrapper.UserInfoWrapper;
import com.multibankfx.chatapp.exception.MfxException;
import com.multibankfx.chatapp.service.ILoginService;
import com.multibankfx.chatapp.data.dto.UserInfoDto;
import com.multibankfx.chatapp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Slf4j
@Component
public class RegisterUserFacade {

    @Autowired
    private IUserService userService;

    @Autowired
    private ILoginService loginService;

    @Autowired
    private UserInfoWrapper userInfoWrapper;

    /**
     *
     * @param username
     * @param name
     * @param surname
     * @param password
     * @return
     */
    @Transactional
    public UserInfoDto register(String username, String name, String surname, String password) {

        //todo validate
        if(StringUtils.isEmpty(username)) {
            throw new MfxException(400,"username cant be empty");
        }

        if(userService.findByUsername(username) != null) {
            throw new MfxException(400,"username already exists");
        }

        User user = userService.createUser(username, name, surname);

        loginService.createUserSecurity(user.getId(), password);

        return userInfoWrapper.get(user);
    }


}
