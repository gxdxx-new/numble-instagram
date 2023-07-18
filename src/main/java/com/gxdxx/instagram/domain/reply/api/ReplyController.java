package com.gxdxx.instagram.domain.reply.api;

import com.gxdxx.instagram.domain.reply.dto.request.ReplyRegisterRequest;
import com.gxdxx.instagram.domain.reply.dto.request.ReplyUpdateRequest;
import com.gxdxx.instagram.domain.reply.dto.response.ReplyRegisterResponse;
import com.gxdxx.instagram.domain.reply.dto.response.ReplyUpdateResponse;
import com.gxdxx.instagram.dto.response.*;
import com.gxdxx.instagram.global.dto.response.ErrorResponse;
import com.gxdxx.instagram.global.dto.response.SuccessResponse;
import com.gxdxx.instagram.global.error.InvalidRequestException;
import com.gxdxx.instagram.domain.reply.application.ReplyCreateService;
import com.gxdxx.instagram.domain.reply.application.ReplyDeleteService;
import com.gxdxx.instagram.domain.reply.application.ReplyUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

@Tag(name = "replies", description = "답글 API")
@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
public class ReplyController {

    private final ReplyCreateService replyCreateService;
    private final ReplyUpdateService replyUpdateService;
    private final ReplyDeleteService replyDeleteService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "답글 작성 성공",
                    content = @Content(schema = @Schema(implementation = ReplyRegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "답글 작성 메소드", description = "답글 작성 메소드입니다.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReplyRegisterResponse createReply(
            @RequestBody @Valid ReplyRegisterRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return replyCreateService.createReply(request, principal.getName());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답글 수정 성공",
                    content = @Content(schema = @Schema(implementation = ReplyUpdateResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "답글 수정 메소드", description = "답글 수정 메소드입니다.")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReplyUpdateResponse updateReply(
            @RequestBody @Valid ReplyUpdateRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return replyUpdateService.updateReply(request, principal.getName());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "답글 삭제 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "답글 삭제 메소드", description = "답글 삭제 메소드입니다.")
    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse deleteReply(
            @Parameter(name = "id", description = "답글의 id", in = ParameterIn.PATH) @PathVariable("id") Long id,
            Principal principal
    ) {
        return replyDeleteService.deleteReply(id, principal.getName());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

}
