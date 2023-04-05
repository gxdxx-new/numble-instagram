package com.gxdxx.instagram.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 클라이언트가 요청을 보내면 딱 한번 실행이 된다.
 * 클라이언트가 header에 토큰값을 실어보내면 doFilterInternal안에서 토큰검증 후 인증객체 생성 후 Security Context에 정보 저장
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    // HTTP 요청이 오면 WAS(tomcat)가 HttpServletRequest, HttpServletResponse 객체를 만들어 줌
    // 만든 인자 값을 받아옴
    // 요청이 들어오면 diFilterInternal이 딱 한번 실행됨
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // WebSecurityConfig 에서 보았던 UsernamePasswordAuthenticationFilter 보다 먼저 동작

        // Access / Refresh 헤더에서 토큰을 가져온다.
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

        if(accessToken != null) {
            // Access 토큰값이 유효하다면 setAuthentication를 통해 security context에 인증 정보저장
            if(jwtUtil.tokenValidation(accessToken)){
                setAuthentication(jwtUtil.getNicknameFromToken(accessToken));
            }
            // Access 토큰이 만료된 상황 && Refresh 토큰 또한 존재하는 상황
            else if (refreshToken != null) {
                // Refresh 토큰 검증 && Refresh 토큰 DB에서  토큰 존재 유무 확인
                boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
                // Refresh 토큰이 유효하고 Refresh 토큰이 DB와 비교했을때 같다면
                if (isRefreshToken) {
                    // Refresh 토큰으로 아이디 정보 가져오기
                    String loginId = jwtUtil.getNicknameFromToken(refreshToken);
                    // 새로운 Access 토큰 발급
                    String newAccessToken = jwtUtil.createToken(loginId, "Access");
                    // 헤더에 Access 토큰 추가
                    jwtUtil.setHeaderAccessToken(response, newAccessToken);
                    // Security context에 인증 정보 넣기
                    setAuthentication(jwtUtil.getNicknameFromToken(newAccessToken));
                } else {
                    // Refresh 토큰이 만료 || Refresh 토큰이 DB와 비교했을때 똑같지 않다면
                    jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                    return;
                }
            }
        }

        filterChain.doFilter(request,response);
    }

    // SecurityContext 에 Authentication 객체를 저장
    public void setAuthentication(String email) {
        Authentication authentication = jwtUtil.createAuthentication(email);
        // security가 만들어주는 securityContextHolder 안에 authentication을 넣어줌
        // security가 securitycontextholder에서 인증 객체를 확인하는데
        // jwtAuthfilter에서 authentication을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않음
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Jwt 예외처리
    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SuccessResponse(msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
