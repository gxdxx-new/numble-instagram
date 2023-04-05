package com.gxdxx.instagram.security;

import com.gxdxx.instagram.jwt.JwtAuthFilter;
import com.gxdxx.instagram.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors();
        /**
         * csrf를 비활성화해 Post 요청이 가능하도록 함
         * csrf는 특정 웹사이트에 공격 방법 중 하나이고, 여기서 설정하는 csrf는 이를 방지하기 위해 csrf토큰을 통해 인증을 하는 것
         */
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /**
         * 회원가입과 로그인을 위한 /api/users/signup, /api/users/login 로 들어오는 요청은 검증없이 요청을 허용하도록 설정
         */
        http.authorizeRequests()
                .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
