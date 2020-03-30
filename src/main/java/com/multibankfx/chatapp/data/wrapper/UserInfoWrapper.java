package com.multibankfx.chatapp.data.wrapper;

import com.multibankfx.chatapp.data.collection.User;
import com.multibankfx.chatapp.data.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserInfoWrapper {

    public UserInfoDto get(User user) {
        UserInfoDto dto = new UserInfoDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setUsername(user.getUsername());
        dto.setUserStatus(user.getUserStatus());
        return dto;
    }

}
