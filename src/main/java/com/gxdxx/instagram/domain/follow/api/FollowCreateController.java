package com.gxdxx.instagram.domain.follow.api;

import com.gxdxx.instagram.domain.follow.application.FollowCreateService;
import com.gxdxx.instagram.domain.follow.dto.request.FollowCreateRequest;
import com.gxdxx.instagram.global.common.dto.response.ErrorResponse;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.global.error.InvalidRequestException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "follows", description = "팔로우 API")
@RequiredArgsConstructor
@RequestMapping("/api/follows")
@RestController
public class FollowCreateController {

    private final FollowCreateService followCreateService;

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
        return followCreateService.createFollow(request, principal.getName());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

}
