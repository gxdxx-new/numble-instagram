package com.gxdxx.instagram.domain.admin;

import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminController {

    @GetMapping
    public SuccessResponse admin() {
        return new SuccessResponse("200 SUCCESS");
    }

}
