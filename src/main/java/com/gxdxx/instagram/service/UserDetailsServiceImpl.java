package com.gxdxx.instagram.service;

import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.entity.UserDetailsImpl;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

// UserDetailsService: 스프링 시큐리티에서 로그인을 진행할 때 사용자 정보를 가져오는 인터페이스
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // 사용자 닉네임으로 사용자의 정보를 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(
                () -> new UserNotFoundException()
        );

        return UserDetailsImpl.of(user);
    }

}
