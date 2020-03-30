package com.multibankfx.chatapp.controller.rest;

import com.multibankfx.chatapp.service.IConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController(value = "/conversation")
public class ConversationController  {

    @Autowired
    private IConversationService conversationService;

}
