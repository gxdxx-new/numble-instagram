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
        User user = findUserByAuthentication();
        return hasAuthority(user, permission);
    }

    private User findUserByAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByNicknameWithAuthorities(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
    }

    private boolean hasAuthority(User user, String permission) {
        return user.getUserAuthorities()
                .stream()
                .anyMatch(userAuthority -> userAuthority.getAuthority().getName().equals(permission));
    }

}
