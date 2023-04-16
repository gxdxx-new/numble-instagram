package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.dto.response.PostRegisterResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostRegisterResponse registerPost(
            PostRegisterRequest request,
            BindingResult bindingResult,
            Principal principal
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
        return postService.registerPost(request, principal.getName());
    }

}
