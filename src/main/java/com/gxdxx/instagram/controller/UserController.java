package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.UserSignUpRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.dto.response.UserSignUpResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserSignUpResponse registerUser(
            @Valid UserSignUpRequest request, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
        return userService.saveUser(request);
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteUser(@PathVariable("id") Long id) {
        // TODO: JWT 토큰의 ID값으로 유저가 맞는지 확인
        return userService.deleteUser(id);
    }

}
