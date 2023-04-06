package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.FollowCreateRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.FollowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/follows")
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public SuccessResponse createFollow(
            @RequestBody @Valid FollowCreateRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }

        return followService.createFollow(request, principal.getName());
    }

}
