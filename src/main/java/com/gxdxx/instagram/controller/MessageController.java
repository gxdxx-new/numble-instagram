package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.MessageSendRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/messages")
@RestController
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public SuccessResponse sendMessage(
            @RequestBody @Valid MessageSendRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }

        return messageService.sendMessage(request, principal.getName());
    }

}
