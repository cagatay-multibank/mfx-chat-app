package com.multibankfx.chatapp.exception;

import com.multibankfx.chatapp.controller.response.ControllerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@ControllerAdvice
public class MfxExceptionHandler {

    @Value("spring.profiles.active")
    private static String profile;


    /**
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleAnyException(Exception e, HttpServletRequest request, HttpServletResponse response) {

        log.error("handleAnyException e:", e);

        ControllerResponse body = new ControllerResponse();

        if (e instanceof AccessDeniedException) {
            body.setResult(HttpStatus.FORBIDDEN.value(), "Access Denied ");
            return new ResponseEntity<ControllerResponse>(body, HttpStatus.FORBIDDEN);
        }

        if (e instanceof AccessDeniedException) {
            body.setResult(HttpStatus.FORBIDDEN.value(), "Access Denied ");
            return new ResponseEntity<ControllerResponse>(body, HttpStatus.FORBIDDEN);
        }

        if(e instanceof MfxException) {
            body.setResult(((MfxException) e).getCode(),((MfxException) e).getError());
        }
        if("local".equals(profile))
        body.setStacktrace(ExceptionUtils.getStackTrace(e));

        return new ResponseEntity<ControllerResponse>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
