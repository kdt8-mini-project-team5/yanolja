package com.yanolja.yanolja.domain.auth.config.filter;


import com.yanolja.yanolja.domain.auth.config.model.CustomUserDetails;
import com.yanolja.yanolja.domain.auth.config.model.UserRole;
import com.yanolja.yanolja.global.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "Request 필터")
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_ROLE = "role";
    private final JwtTokenUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        String tokenValue = jwtUtil.getTokenFromRequest(req);
        log.info("요청 URL : " + req.getRequestURL());
        if (StringUtils.hasText(tokenValue)) {
            tokenValue = jwtUtil.substringToken(tokenValue);

            if (!jwtUtil.validateToken(tokenValue, res)) {
                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        res.addCookie(cookie);
                    }
                }

                res.setStatus(401);
                res.sendRedirect("/login");
                res.getWriter().flush();
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
            try {
                setAuthentication(info.getSubject(), info.get(AUTHORIZATION_ROLE).toString());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    public void setAuthentication(String email, String role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email, role);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String email, String role) {
        CustomUserDetails userDetails = new CustomUserDetails(null, email, null, UserRole.valueOf(role));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
