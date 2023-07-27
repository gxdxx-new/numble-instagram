package com.gxdxx.instagram.domain.admin;

import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.domain.UserAuthority;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PermissionService {

    private final UserRepository userRepository;

    public boolean hasPermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 인증 정보를 통해 사용자 정보를 가져옴
        User user = userRepository.findByNicknameWithAuthorities(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        // 사용자가 해당 권한을 가지고 있는지 확인
        for (UserAuthority userAuthority : user.getUserAuthorities()) {
            if (userAuthority.getAuthority().getName().equals(permission)) {
                return true;
            }
        }
        return false;
    }

}
