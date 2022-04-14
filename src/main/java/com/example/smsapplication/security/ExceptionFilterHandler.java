package com.example.smsapplication.security;

import com.example.smsapplication.dtos.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionFilterHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            log.info("Exception -> {}", exception.getMessage());
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, exception);
        }
    }


    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable exception) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        APIResponse apiResponse = new APIResponse("", exception.getMessage());
        try {
            String JsonOutput = apiResponse.convertToJson();
            response.getWriter().write(JsonOutput);
        } catch (IOException e) {
            log.info("Exception --> {}", exception.getMessage());
        }
    }
}