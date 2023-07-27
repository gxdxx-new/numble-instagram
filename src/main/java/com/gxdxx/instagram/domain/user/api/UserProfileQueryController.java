package com.gxdxx.instagram.domain.user.api;

import com.gxdxx.instagram.domain.user.application.UserProfileQueryService;
import com.gxdxx.instagram.domain.user.dto.response.UserProfileResponse;
import com.gxdxx.instagram.global.common.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "users", description = "회원 API")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserProfileQueryController {

    private final UserProfileQueryService userProfileQueryService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "프로필 조회 메소드", description = "프로필 조회 메소드입니다.")
    @GetMapping(value = "/profile", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfileResponse findUserProfile(Principal principal) {
        return userProfileQueryService.findUserProfile(principal.getName());
    }

}
