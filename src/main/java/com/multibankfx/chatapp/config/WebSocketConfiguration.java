package com.multibankfx.chatapp.config;

import com.multibankfx.chatapp.config.security.JwtTokenUtil;
import com.multibankfx.chatapp.data.dto.StompPrincipal;
import com.multibankfx.chatapp.data.dto.UserSessionDto;
import com.multibankfx.chatapp.hazelcast.HazelcastCacheStore;
import com.multibankfx.chatapp.service.impl.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private static final String SESSION_ID_FIELD = "sessionId";

    @Autowired
    @Qualifier(value = "hzUserSessions")
    private HazelcastCacheStore<UserSessionDto> hzUserSessions;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*").
                setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {
                        // Generate principal with UUID as name
                        return new StompPrincipal(attributes.get(SESSION_ID_FIELD).toString());
                    }

                    public boolean beforeHandshake(
                            ServerHttpRequest request,
                            ServerHttpResponse response,
                            WebSocketHandler wsHandler,
                            Map attributes) throws Exception {

                        if (request instanceof ServletServerHttpRequest) {
                            ServletServerHttpRequest servletRequest
                                    = (ServletServerHttpRequest) request;
                            HttpSession session = servletRequest
                                    .getServletRequest().getSession();

                            attributes.put(SESSION_ID_FIELD, session.getId());
                        }
                        return true;
                    }
                }).withSockJS().setInterceptors(httpSessionHandshakeInterceptor());
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(30 * 1000).setSendBufferSizeLimit(128 * 1024 * 1024).setMessageSizeLimit(1202400* 1024).
                addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
                    @Override
                    public WebSocketHandler decorate(final WebSocketHandler handler) {
                        return new WebSocketHandlerDecorator(handler) {
                            @Override
                            public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                                log.info("ws session: " + session.getId());
                                super.afterConnectionEstablished(session);
                            }

                            @Override
                            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                                UserSessionDto userSession = hzUserSessions.get(session.getId());
                                if (userSession != null)
                                    userSession.setLastUpdateTime(System.currentTimeMillis());

                                super.handleMessage(session, message);
                            }
                        };
                    }
                });
        ;
    }

    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {

            @Autowired
            private LoginService loginService;

            @Autowired
            private JwtTokenUtil jwtTokenUtil;

            @Override
            public boolean beforeHandshake(
                    ServerHttpRequest request,
                    ServerHttpResponse response,
                    WebSocketHandler wsHandler,
                    Map attributes) throws Exception {


                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest
                            = (ServletServerHttpRequest) request;
                    HttpSession session = servletRequest
                            .getServletRequest().getSession();

                    String username = "";
                    String token = "";
                    Boolean authenticated = false;
                    try {
                        username = servletRequest.getServletRequest().getParameter("username").toString();
                        token = servletRequest.getServletRequest().getParameter("token").toString();

                        log.info("username: " + username);
                        log.info("token: " + token);

                        UserDetails userDetails = loginService.loadUserByUsername(username);
                        authenticated = jwtTokenUtil.validateToken(token, userDetails);
                    } catch (Exception e) {

                    }

                    if (authenticated != null && authenticated.booleanValue()) {
                        try {
                            String websocketSessionId = "";
                            if (servletRequest.getURI() == null ||
                                    servletRequest.getURI().getPath() == null ||
                                    servletRequest.getURI().getPath().split("/").length < 4)
                                return false;

                            websocketSessionId = servletRequest.getURI().getPath().split("/")[3];
                            hzUserSessions.put(websocketSessionId, new UserSessionDto(websocketSessionId, username, token, session.getId()));
                        } catch (Exception e) {
                            return false;
                        }

                        attributes.put("sessionId", session.getId());
                        return true;
                    } else {
                        return false;
                    }
                }
                return true;
            }


            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                //  TODO : this will implemented later, this comment is for sonarqube
            }
        };
    }

}
