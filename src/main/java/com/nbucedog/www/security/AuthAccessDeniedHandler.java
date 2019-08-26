package com.nbucedog.www.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbucedog.www.util.ResultTools;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public AuthAccessDeniedHandler(ObjectMapper objectMapper){
        this.objectMapper=objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException{
//        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ResultTools.dataResult(403,e.getMessage())));
    }
}
