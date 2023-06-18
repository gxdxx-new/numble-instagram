package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.ChatRoomListRequest;
import com.gxdxx.instagram.dto.response.ChatRoomResponse;
import com.gxdxx.instagram.dto.response.CommentRegisterResponse;
import com.gxdxx.instagram.dto.response.ErrorResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "chat-rooms", description = "채팅방 API")
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "채팅방 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "채팅방 조회 메소드", description = "채팅방 조회 메소드입니다.")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChatRoomResponse getChatRooms(
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
