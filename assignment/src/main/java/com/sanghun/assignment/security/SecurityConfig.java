package com.sanghun.assignment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
            .authorizeHttpRequests(
                    authorizeRequests -> authorizeRequests
                            .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 경로 허용
                            .requestMatchers("/signup").permitAll()
                            .requestMatchers("/sign").permitAll()
                            .requestMatchers("/error").permitAll() // 에러 페이지 요청 허가
                            .requestMatchers("/swagger", "/swagger-ui.html",
                                    "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**")
                            .permitAll() // Swagger 페이지 요청 허가

            )
            .headers(headers -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션
                                                                                                          // 비활성화
            .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
            .httpBasic(AbstractHttpConfigurer::disable); // HTTP Basic 인증 비활성화

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
