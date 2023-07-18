package com.gxdxx.instagram.domain.message.api;

import com.gxdxx.instagram.domain.message.dto.request.MessageListRequest;
import com.gxdxx.instagram.domain.message.dto.request.MessageSendRequest;
import com.gxdxx.instagram.global.common.dto.response.ErrorResponse;
import com.gxdxx.instagram.domain.message.dto.response.MessageResponse;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.global.error.InvalidRequestException;
import com.gxdxx.instagram.domain.message.application.MessageCreateService;
import com.gxdxx.instagram.domain.message.application.MessageQueryService;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "messages", description = "메시지 API")
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@RestController
public class MessageController {

    private final MessageCreateService messageCreateService;
    private final MessageQueryService messageQueryService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "메시지 전송 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "메시지 전송 메소드", description = "메시지 전송 메소드입니다.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse createMessage(
            @RequestBody @Valid MessageSendRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return messageCreateService.sendMessage(request, principal.getName());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 조회 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "메시지 조회 메소드", description = "메시지 조회 메소드입니다.")
    @GetMapping(value = "/{chat-room-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageResponse findMessages(
            @RequestBody @Valid MessageListRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return messageQueryService.findMessages(request, principal.getName());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

}
