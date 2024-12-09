package com.junho.springevaluation.configure;

import com.junho.springevaluation.component.CustomAuccessDeniedHandler;
import com.junho.springevaluation.component.CustomAuthenticationEntryPoint;
import com.junho.springevaluation.jwt.JwtFilter;
import com.junho.springevaluation.jwt.JwtUtil;
import com.junho.springevaluation.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration // 이 클래스가 Spring의 설정 클래스임을 나타냄
@EnableWebSecurity // Spring Security를 활성화
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration; // Authentication 매니저 설정을 위한 구성
    private final JwtUtil jwtUtil; // JWT 유틸리티 클래스
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAuccessDeniedHandler customAcessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // AuthenticationManager를 빈으로 등록
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 인코딩을 위한 BCryptPasswordEncoder 빈 등록
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // HTTP 보안 설정
        http.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .formLogin(formLogin -> formLogin.disable()) // 폼 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP 기본 인증 비활성화
                .authorizeHttpRequests(auth -> // 요청 권한 설정
                        auth.requestMatchers("/", "/login", "/join").permitAll() // 특정 경로 허용
                                .requestMatchers("/admin").hasRole("ADMIN") // /admin 경로는 ADMIN 역할만 접근 가능
                                .anyRequest().authenticated()); // 그 외의 요청은 인증 필요

        http.cors(cors -> cors.configurationSource(request -> { // CORS 설정
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001")); // 허용할 출처
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
            config.setAllowCredentials(true); // 자격 증명 전송 허용
            config.addExposedHeader("Authorization"); // 클라이언트가 접근할 수 있는 응답 헤더
            config.addAllowedHeader("*"); // 모든 요청 헤더 허용
            return config; // 설정 반환
        }));

        http.sessionManagement(session -> // 세션 관리 설정
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 상태 비저장 세션 설정

        http.addFilterBefore(new JwtFilter(this.jwtUtil), LoginFilter.class); // LoginFilter 이전에 JwtFilter 추가

        http.addFilterAt(new LoginFilter(authenticationManager(this.authenticationConfiguration), this.jwtUtil),
                UsernamePasswordAuthenticationFilter.class); // UsernamePasswordAuthenticationFilter 위치에 LoginFilter 추가

        http.exceptionHandling(exception -> {
            exception.authenticationEntryPoint(this.customAuthenticationEntryPoint);
            exception.accessDeniedHandler(this.customAcessDeniedHandler);
        });

        return http.build(); // 보안 필터 체인 빌드 및 반환
    }
}
