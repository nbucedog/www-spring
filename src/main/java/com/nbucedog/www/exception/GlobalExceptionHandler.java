package com.nbucedog.www.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbucedog.www.util.CookieTools;
import com.nbucedog.www.util.ResultTools;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(value = SpelEvaluationException.class)
    public void spelEvaluationException(HttpServletResponse response,SpelEvaluationException e) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.addCookie(CookieTools.buildCookie("username","",0,false));
        response.addCookie(CookieTools.buildCookie("nickname","",0,false));
        response.addCookie(CookieTools.buildCookie("id","",0,false));
        response.getWriter().write(objectMapper.writeValueAsString(ResultTools.dataResult(403,e.getMessage())));
    }
}
