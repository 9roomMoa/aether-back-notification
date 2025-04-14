package com.groommoa.aether_back_notification.global.auth.filter;

import com.groommoa.aether_back_notification.global.auth.exception.TokenException;
import com.groommoa.aether_back_notification.global.auth.jwt.JwtProvider;
import com.groommoa.aether_back_notification.global.common.constants.TokenKey;
import com.groommoa.aether_back_notification.global.common.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request);

        if (jwtProvider.validateToken(accessToken)){

            setAuthentication(accessToken);
        } else {
            throw new TokenException(ErrorCode.TOKEN_EXPIRED);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 토큰을 검증한 후 SecurityContext에 인증 정보를 저장
     *
     * @param accessToken 유효한 JWT 액세스 토큰
     */
    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 요청 헤더에서 JWT 토큰 추출.
     *
     * @param request HTTP 요청 객체
     * @return 추출된 토큰 (Bearer 제거 후)
     */
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith(TokenKey.TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TokenKey.TOKEN_PREFIX.length());
    }
}
