package com.multibankfx.chatapp.service.impl;

import com.multibankfx.chatapp.data.collection.User;
import com.multibankfx.chatapp.data.collection.UserSecurity;
import com.multibankfx.chatapp.data.collection.UserSession;
import com.multibankfx.chatapp.data.enumarated.SessionStatus;
import com.multibankfx.chatapp.data.enumarated.Status;
import com.multibankfx.chatapp.data.enumarated.UserStatus;
import com.multibankfx.chatapp.data.repository.UserRepository;
import com.multibankfx.chatapp.data.repository.UserSecurityRepository;
import com.multibankfx.chatapp.data.repository.UserSessionRepository;
import com.multibankfx.chatapp.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
public class LoginService implements ILoginService, UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSecurityRepository userSecurityRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    @Qualifier(value = "bcryptEncoder")
    private PasswordEncoder passwordEncoder;


    @Override
    public boolean checkPasswords(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }

    @Override
    public void clearUserSecurityInformations(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update().set("status",  Status.PASSIVE);
        mongoTemplate.findAndModify(query, update, UserSecurity.class);
        // todo evict clearUserSecurityInformations hazelcast
    }

    @Override
    public void clearUserSessions(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update().set("sessionStatus",  SessionStatus.CANCELLED);
        mongoTemplate.findAndModify(query, update, UserSession.class);;
    }

    @Override
    public UserSession createUserSession(String userId, String sid) {
        UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        userSession.setSid(sid);
        userSession.setSessionStatus(SessionStatus.ACTIVE);
        userSession = userSessionRepository.save(userSession);
        return userSession;
    }

    @Override
    public UserSecurity createUserSecurity(String userId, String password) {
        // todo validate password
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUserId(userId);
        userSecurity.setPassword(passwordEncoder.encode(password));
        userSecurity.setStatus(Status.ACTIVE);
        userSecurity = userSecurityRepository.save(userSecurity);
        return userSecurity;
    }

    @Override
    public boolean checkPasswordByUserId(String userId, String password) {
        List<UserSecurity> userSecurities = userSecurityRepository.findUserSecuritiesByUserIdAndStatus(userId, Status.ACTIVE);
        if (CollectionUtils.isEmpty(userSecurities)) {
            return false;
        }

        final UserSecurity userSecurity = userSecurities.get(0);
        return passwordEncoder.matches(password, userSecurity.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("invalid username");
        }

        try {
            final User user = userRepository.findUserByUsernameAndUserStatus(username, UserStatus.ACTIVE);

            List<UserSecurity> userSecurities = userSecurityRepository.findUserSecuritiesByUserIdAndStatus(user.getId(), Status.ACTIVE);

            if (CollectionUtils.isEmpty(userSecurities)) {
                return null;
            }

            final UserSecurity userSecurity = userSecurities.get(0);

            return new org.springframework.security.core.userdetails.User(user.getUsername(), userSecurity.getPassword(), true, true, true, true, new ArrayList<>());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

    }
}
