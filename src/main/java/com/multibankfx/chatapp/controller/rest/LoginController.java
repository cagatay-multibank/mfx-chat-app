package com.multibankfx.chatapp.controller.rest;

import com.multibankfx.chatapp.config.security.JwtTokenUtil;
import com.multibankfx.chatapp.data.collection.User;
import com.multibankfx.chatapp.exception.MfxException;
import com.multibankfx.chatapp.service.impl.LoginService;
import com.multibankfx.chatapp.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    @Qualifier(value = "authProvider")
    private DaoAuthenticationProvider authProvider;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        final UserDetails userDetails = loginService.loadUserByUsername(loginRequest.getUsername());
        User user = userService.findByUsername(loginRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        loginService.clearUserSessions(user.getId());
        loginService.createUserSession(user.getId(), token);
        return ResponseEntity.ok(new LoginResponse(loginRequest.getUsername(), token));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new MfxException(400,"invalid credentials");
        }
    }

    @Getter
    @Setter
    private static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class LoginResponse {
        private String username;
        private String token;
    }
}
