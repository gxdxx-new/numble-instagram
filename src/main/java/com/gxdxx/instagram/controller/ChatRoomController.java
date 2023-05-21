package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.ChatRoomListRequest;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public Map<String, Object> getChatRooms(
            @RequestBody @Valid ChatRoomListRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return chatRoomService.getChatRooms(request, principal.getName());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

}
