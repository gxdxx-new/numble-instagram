package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.FollowCreateRequest;
import com.gxdxx.instagram.dto.response.ErrorResponse;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.FollowService;
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

@Tag(name = "follows", description = "팔로우 API")
@RequiredArgsConstructor
@RequestMapping("/api/follows")
@RestController
public class FollowController {

    private final FollowService followService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "팔로우 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팔로우 메소드", description = "팔로우 메소드입니다.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse createFollow(
            @RequestBody @Valid FollowCreateRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return followService.createFollow(request, principal.getName());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "팔로우 취소 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "팔로우 취소 메소드", description = "팔로우 취소 메소드입니다.")
    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse deleteFollow(
            @Parameter(name = "id", description = "게시글의 id", in = ParameterIn.PATH) @PathVariable("id") Long id,
            Principal principal
    ) {
        return followService.deleteFollow(id, principal.getName());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

}
