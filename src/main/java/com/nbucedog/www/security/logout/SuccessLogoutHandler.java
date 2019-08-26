package com.nbucedog.www.security.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbucedog.www.dao.entity.User;
import com.nbucedog.www.service.UserService;
import com.nbucedog.www.util.CookieTools;
import com.nbucedog.www.util.ResultTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SuccessLogoutHandler implements LogoutSuccessHandler {
    @Autowired
    UserService userService;

    private final ObjectMapper objectMapper;

    public SuccessLogoutHandler(ObjectMapper objectMapper){
        this.objectMapper=objectMapper;
    }
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)throws IOException{
        User user = userService.findByUsername(authentication.getName());
        int maxAge=0;
        response.addCookie(CookieTools.buildCookie("username",user.getUsername(),maxAge,false));
        response.setStatus(HttpStatus.OK.value());
        response.addCookie(CookieTools.buildCookie("nickname",user.getNickname(),maxAge,false));
        response.addCookie(CookieTools.buildCookie("id",String.valueOf(user.getId()),maxAge,false));

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ResultTools.dataResult(101,authentication)));
    }
}
