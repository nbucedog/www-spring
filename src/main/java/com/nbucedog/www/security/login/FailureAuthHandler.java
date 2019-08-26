package com.nbucedog.www.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbucedog.www.util.ResultTools;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FailureAuthHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    public FailureAuthHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)throws IOException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setStatus(HttpStatus.OK.value());

        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods",request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ResultTools.dataResult(402,e.getMessage())));
    }
}
