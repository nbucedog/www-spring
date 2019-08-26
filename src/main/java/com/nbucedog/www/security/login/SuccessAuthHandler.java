package com.nbucedog.www.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbucedog.www.dao.entity.User;
import com.nbucedog.www.service.UserService;
import com.nbucedog.www.util.CookieTools;
import com.nbucedog.www.util.ResultTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SuccessAuthHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper;

    public SuccessAuthHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods",request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials","true");

        User user = userService.findByUsername(authentication.getName());
        int maxAge=60*60*24;
        response.addCookie(CookieTools.buildCookie("username",user.getUsername(),maxAge,false));
        response.addCookie(CookieTools.buildCookie("nickname",user.getNickname(),maxAge,false));
        response.addCookie(CookieTools.buildCookie("id",String.valueOf(user.getId()),maxAge,false));
        response.getWriter().write(objectMapper.writeValueAsString(ResultTools.dataResult(100,authentication)));
    }
}
