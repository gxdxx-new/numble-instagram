package com.gxdxx.instagram.domain.admin.api;

import com.gxdxx.instagram.domain.admin.application.AdminUserQueryService;
import com.gxdxx.instagram.domain.admin.dto.request.AdminUserQueryRequest;
import com.gxdxx.instagram.domain.admin.dto.response.AdminUserQueryResponse;
import com.gxdxx.instagram.global.error.InvalidRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminUserQueryController {

    private final AdminUserQueryService adminUserQueryService;

    @PreAuthorize("@permissionService.hasPermission('READ_USER')")
    @GetMapping(value = "/users")
    public Page<AdminUserQueryResponse> findUsers(
            @PageableDefault Pageable pageable,
            @RequestBody @Valid AdminUserQueryRequest request,
            BindingResult bindingResult
    ) {
        validateRequest(bindingResult);
        return adminUserQueryService.findUsers(request.roleName(), pageable.getPageNumber(), pageable.getPageSize());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

}
