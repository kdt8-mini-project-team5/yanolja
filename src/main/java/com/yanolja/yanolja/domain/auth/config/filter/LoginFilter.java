package com.yanolja.yanolja.domain.auth.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanolja.yanolja.domain.auth.config.model.CustomUserDetails;
import com.yanolja.yanolja.domain.auth.exception.AuthException;
import com.yanolja.yanolja.domain.auth.exception.errorcode.AuthErrorCode;
import com.yanolja.yanolja.domain.auth.model.request.LoginRequest;
import com.yanolja.yanolja.global.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j(topic = "로그인")
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenUtil jwtUtil;

    public LoginFilter(JwtTokenUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new AuthException(AuthErrorCode.LOGIN_FAILED);
        }

    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {
        log.info("로그인 성공");
        String email = ((CustomUserDetails) authResult.getPrincipal()).getUsername();

        String token = jwtUtil.createToken(email, ((CustomUserDetails) authResult.getPrincipal()).getUserRole().getAuth());
        jwtUtil.addJwtToCookie(token, response);

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        writer.flush();
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException {

        log.info("로그인 실패");
        response.setStatus(401);
        PrintWriter writer = response.getWriter();
        writer.flush();
    }

}
