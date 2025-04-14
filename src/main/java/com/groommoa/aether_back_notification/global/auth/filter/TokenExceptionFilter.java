package com.groommoa.aether_back_notification.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groommoa.aether_back_notification.global.auth.exception.TokenException;
import com.groommoa.aether_back_notification.global.common.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Component
public class TokenExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenException ex){
            ErrorResponse errorResponse = new ErrorResponse(
                    ex.getErrorCode().getHttpStatus().value(),
                    ex.getMessage(),
                    null
            );

            response.setStatus(ex.getErrorCode().getHttpStatus().value());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
