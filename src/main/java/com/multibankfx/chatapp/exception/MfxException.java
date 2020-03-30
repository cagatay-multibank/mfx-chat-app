package com.multibankfx.chatapp.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MfxException extends RuntimeException {

    private int code = -999;
    private String error = null;

    public MfxException(int code, String error) {
        this.code = code;
        this.error = error;
    }
}
