package com.multibankfx.chatapp.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

public class MfxDaoAuthenticationProvider extends DaoAuthenticationProvider {



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UserDetails user = getUserDetailsService().loadUserByUsername(authentication.getName());

        if ((user == null)) {
            throw new BadCredentialsException("Invalid username or password");
        }
        final Authentication result = super.authenticate(authentication);
        return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
    }
}
