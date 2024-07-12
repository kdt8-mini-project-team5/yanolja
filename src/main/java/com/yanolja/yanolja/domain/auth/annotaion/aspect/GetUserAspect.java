package com.yanolja.yanolja.domain.auth.annotaion.aspect;

import com.yanolja.yanolja.domain.auth.annotaion.GetUser;
import com.yanolja.yanolja.domain.auth.config.model.CustomUserDetails;
import com.yanolja.yanolja.domain.auth.exception.AuthException;
import com.yanolja.yanolja.domain.auth.exception.errorcode.AuthErrorCode;
import com.yanolja.yanolja.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class GetUserAspect {

    private final UserRepository userRepository;

    @Around("@annotaion(getUser)")
    public Object getUserAspect(ProceedingJoinPoint joinPoint, GetUser getUser) throws Throwable{

        Object[] args = joinPoint.getArgs();

        boolean check = true;

        for (Object arg : args) {
            if (arg instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) arg;
                String email = customUserDetails.getEmail();
                customUserDetails.setUser(userRepository.findByEmail(email).orElseThrow(() -> new AuthException(AuthErrorCode.NOT_FOUND)));
                check = false;
            }
        }

        if (check)
            throw new AuthException(AuthErrorCode.NOT_FOUND);

        return joinPoint.proceed(args);
    }

}
