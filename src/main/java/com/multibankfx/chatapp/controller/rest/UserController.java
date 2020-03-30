package com.multibankfx.chatapp.controller.rest;

import com.multibankfx.chatapp.data.dto.UserInfoDto;
import com.multibankfx.chatapp.facade.RegisterUserFacade;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@Slf4j
@RestController
@RequestMapping(value = "user" )
public class UserController {

    @Autowired
    private RegisterUserFacade registerUserFacade;

    @PostMapping(value = "/register")
    public @ResponseBody
    ResponseEntity<UserInfoDto> register(@RequestBody UserController.RegisterUserRequest registerUserRequest) {
        return new ResponseEntity<>(registerUserFacade.register(registerUserRequest.getUsername(), registerUserRequest.getName(), registerUserRequest.getSurname(),
                registerUserRequest.getPassword())
                        , HttpStatus.OK);
    }

    @Getter
    @Setter
    private static class RegisterUserRequest implements Serializable {

        private String username;

        private String name;

        private String surname;

        private String password;

        public RegisterUserRequest() {
        }
    }
}
