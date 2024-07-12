package com.yanolja.yanolja.domain.auth.service;

import com.yanolja.yanolja.domain.auth.config.model.CustomUserDetails;
import com.yanolja.yanolja.domain.auth.exception.AuthException;
import com.yanolja.yanolja.domain.auth.exception.errorcode.AuthErrorCode;
import com.yanolja.yanolja.domain.auth.model.entity.User;
import com.yanolja.yanolja.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(AuthErrorCode.LOGIN_FAILED));

        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole());
    }

}
