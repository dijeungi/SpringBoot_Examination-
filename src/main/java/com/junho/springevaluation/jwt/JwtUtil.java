package com.junho.springevaluation.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component // 스프링의 컴포넌트로 등록
public class JwtUtil {

    private SecretKey secretKey; // JWT 서명을 위한 비밀 키

    // 생성자: 주입된 비밀 키 문자열을 바이트 배열로 변환하여 SecretKeySpec 객체를 생성
    public JwtUtil(@Value("${authen.jwt.secret.key}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT에서 사용자 이름을 추출하는 메서드
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(this.secretKey).build() // JWT 파서를 생성하고 서명을 검증
                .parseSignedClaims(token).getPayload().get("username", String.class); // 사용자 이름 반환
    }

    // JWT에서 역할(role)을 추출하는 메서드
    public String getRole(String token) {
        return Jwts.parser().verifyWith(this.secretKey).build() // JWT 파서를 생성하고 서명을 검증
                .parseSignedClaims(token).getPayload().get("role", String.class); // 역할 반환
    }

    // JWT의 유효성을 검사하여 만료 여부를 확인하는 메서드
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(this.secretKey).build() // JWT 파서를 생성하고 서명을 검증
                .parseSignedClaims(token).getPayload().getExpiration().before(new Date()); // 현재 날짜와 비교하여 만료 여부 반환
    }

    // JWT를 생성하는 메서드
    public String CreateJWT(String username, String role, Long expireMs) {
        return Jwts.builder() // JWT 빌더 시작
                .claim("username", username) // 사용자 이름을 클레임으로 추가
                .claim("role", role) // 역할을 클레임으로 추가
                .issuedAt(new Date(System.currentTimeMillis())) // 발급 시간 설정
                .expiration(new Date(System.currentTimeMillis() + expireMs)) // 만료 시간 설정
                .signWith(this.secretKey) // 비밀 키를 사용하여 서명
                .compact(); // JWT 문자열로 반환
    }

}
