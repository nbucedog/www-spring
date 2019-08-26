package com.nbucedog.www.util;

import org.springframework.web.util.UriUtils;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;

public class CookieTools {
    public static Cookie buildCookie(String name,String value,int maxAge,boolean httpOnly){
        Cookie cookie = new Cookie(name, UriUtils.encode(value, StandardCharsets.UTF_8));
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        return cookie;
    }
}
