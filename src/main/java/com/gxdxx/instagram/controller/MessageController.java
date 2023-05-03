package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.MessageListRequest;
import com.gxdxx.instagram.dto.request.MessageSendRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

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

    @GetMapping("/{chatRoomId}")
    public Map<String, Object> getMessages(
            @RequestBody @Valid MessageListRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }

        return messageService.getMessages(request, principal.getName());
    }

}
