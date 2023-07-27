package com.gxdxx.instagram.domain.admin;

import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminController {

    @PreAuthorize("@permissionService.hasPermission('READ_USER')")
    @GetMapping
    public SuccessResponse read() {
        return new SuccessResponse("200 SUCCESS");
    }

    @PreAuthorize("@permissionService.hasPermission('WRITE_USER')")
    @PostMapping
    public SuccessResponse write() {
        return new SuccessResponse("200 SUCCESS");
    }

}
