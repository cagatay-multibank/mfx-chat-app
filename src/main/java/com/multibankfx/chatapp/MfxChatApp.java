package com.multibankfx.chatapp;

import com.multibankfx.chatapp.config.StartupInitializerService;
import com.multibankfx.chatapp.data.dto.UserSessionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Hashtable;

@Slf4j
@SpringBootApplication
public class MfxChatApp {

    public static void main(String[] args) {
        SpringApplication.run(MfxChatApp.class, args);
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("APPLICATION INITIALIZED !!! ");
        StartupInitializerService startupInitializerService = event.getApplicationContext().getBean(StartupInitializerService.class);
        startupInitializerService.initializeHazelcast();
        log.info("HAZELCAST INITIALIZED !!! ");
        //if(userService.findByUsername("Furious_Bear") == null)
        //userService.createUser("Furious_Bear","cagatay","gokcel");
    }

}
