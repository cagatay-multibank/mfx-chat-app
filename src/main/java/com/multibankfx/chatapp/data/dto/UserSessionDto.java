package com.multibankfx.chatapp.data.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserSessionDto implements Serializable {

    private String sessionId;
    private String httpSessionId;
    private String username;
    private String token;
    private long loginTime;
    private long lastUpdateTime;

    public UserSessionDto(String sessionId, String username, String token, String httpSessionId) {
        this.sessionId = sessionId;
        this.username = username;
        this.token = token;
        this.httpSessionId = httpSessionId;
        this.loginTime = System.currentTimeMillis();
        this.lastUpdateTime = this.loginTime;
    }

}
