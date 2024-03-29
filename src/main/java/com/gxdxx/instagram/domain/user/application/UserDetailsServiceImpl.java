package com.gxdxx.instagram.domain.user.application;

import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.domain.UserDetailsImpl;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
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
        User user = userRepository.findByNicknameWithRoles(nickname)
                .orElseThrow(UserNotFoundException::new);
        return UserDetailsImpl.of(user);
    }

}
