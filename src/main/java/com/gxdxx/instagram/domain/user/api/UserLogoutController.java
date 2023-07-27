package com.gxdxx.instagram.domain.user.api;

import com.gxdxx.instagram.domain.user.application.UserLogoutService;
import com.gxdxx.instagram.global.common.dto.response.ErrorResponse;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.global.config.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "users", description = "회원 API")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserLogoutController {

    private final UserLogoutService userLogoutService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "로그아웃 메소드", description = "로그아웃 메소드입니다.")
    @PostMapping(value = "/logout", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse logout(
            @CookieValue(TokenProvider.REFRESH_TOKEN) Cookie cookie,
            @RequestHeader(TokenProvider.HEADER_AUTHORIZATION) String authorizationHeader,
            Principal principal
    ) {
        return userLogoutService.logout(cookie.getValue(), authorizationHeader, principal.getName());
    }

}
