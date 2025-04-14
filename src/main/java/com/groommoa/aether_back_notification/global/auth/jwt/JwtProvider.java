package com.groommoa.aether_back_notification.global.auth.jwt;

import com.groommoa.aether_back_notification.global.auth.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.groommoa.aether_back_notification.global.common.exception.ErrorCode.INVALID_JWT_SIGNATURE;
import static com.groommoa.aether_back_notification.global.common.exception.ErrorCode.INVALID_TOKEN;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;

    /**
     * 애플리케이션이 시작될 때 JWT SecretKey 설정
     */
    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    /**
     * JWT 토큰 유효성 검사
     *
     * @param token 검증할 JWT 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date());    // 현재 시간보다 만료 시간이 이후이면 유효
    }

    /**
     * JWT 토큰을 기반으로 Authentication 객체 생성
     *
     * @param token JWT 토큰
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        return new UsernamePasswordAuthenticationToken(claims, token, authorities);
    }

    /**
     * Claims에서 권한 정보를 추출하여 GrantedAuthority 리스트로 변환
     *
     * @param claims JWT의 클레임 정보
     * @return 권한 정보 리스트
     */
    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get("role").toString()));
    }

    /**
     * JWT 토큰을 파싱하여 Claims 객체를 반환
     *
     * @param token 검증할 JWT 토큰
     * @return JWT Claims 객체
     * @throws TokenException 유효하지 않은 토큰일 경우 예외 발생
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e){
            // 만료된 토큰이라도 클레임 정보는 반환 가능
            return e.getClaims();
        } catch (MalformedJwtException e){
            // 잘못된 형식의 토큰 예외 처리
            throw new TokenException(INVALID_TOKEN);
        } catch (SecurityException e){
            // 서명 검증 실패 예외 처리
            throw new TokenException(INVALID_JWT_SIGNATURE);
        }
    }
}
