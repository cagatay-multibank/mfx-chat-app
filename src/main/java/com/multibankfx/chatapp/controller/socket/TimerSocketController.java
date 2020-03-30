package com.multibankfx.chatapp.controller.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TimerSocketController {

    @MessageMapping(value = "/subscribeToTimer")
    public void subscribeToTimer() {
        
    }
}
