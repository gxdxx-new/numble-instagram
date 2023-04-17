package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.CommentRegisterRequest;
import com.gxdxx.instagram.dto.request.CommentUpdateRequest;
import com.gxdxx.instagram.dto.response.CommentRegisterResponse;
import com.gxdxx.instagram.dto.response.CommentUpdateResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentRegisterResponse registerComment(
            @RequestBody @Valid CommentRegisterRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
        return commentService.registerComment(request, principal.getName());
    }

    @PutMapping("/{id}")
    public CommentUpdateResponse updateComment(
            @RequestBody @Valid CommentUpdateRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
        return commentService.updateComment(request, principal.getName());
    }

}
