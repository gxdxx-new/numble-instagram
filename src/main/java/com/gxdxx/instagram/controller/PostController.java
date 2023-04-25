package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.dto.request.PostUpdateRequest;
import com.gxdxx.instagram.dto.response.PostRegisterResponse;
import com.gxdxx.instagram.dto.response.PostUpdateResponse;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostRegisterResponse registerPost(
            @Valid PostRegisterRequest request,
            BindingResult bindingResult,
            Principal principal
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
        return postService.registerPost(request, principal.getName());
    }

    @PutMapping("/{id}")
    public PostUpdateResponse updatePost(
            @Valid PostUpdateRequest request,
            BindingResult bindingResult,
            Principal principal
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
        return postService.updatePost(request, principal.getName());
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deletePost(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        return postService.deletePost(id, principal.getName());
    }

}