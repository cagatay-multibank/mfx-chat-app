package com.multibankfx.chatapp.controller.response;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.HashMap;


@Getter
@Setter
public class ControllerResponse implements Serializable  {

    private HashMap<String,Object> data = new HashMap<String,Object>();
    private String stacktrace;
    private int code;
    private String description =  "success";


    public void setResult(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
