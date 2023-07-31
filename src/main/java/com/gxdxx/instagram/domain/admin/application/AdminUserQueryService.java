package com.gxdxx.instagram.domain.admin.application;

import com.gxdxx.instagram.domain.admin.dto.response.AdminUserQueryResponse;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminUserQueryService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<AdminUserQueryResponse> findUsers(String roleName, int page, int size) {
        return userRepository.findUsersByRole(
                roleName,
                PageRequest.of(page, size)
        );
    }

}
