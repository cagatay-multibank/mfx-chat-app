package com.multibankfx.chatapp.data.dto;

import com.multibankfx.chatapp.data.enumarated.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserInfoDto implements Serializable {

    private String id;

    private String username;

    private String name;

    private String surname;

    private UserStatus userStatus;

}
