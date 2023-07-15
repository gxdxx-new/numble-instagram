package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.config.jwt.TokenProvider;
import com.gxdxx.instagram.dto.request.AccessTokenCreateRequest;
import com.gxdxx.instagram.dto.response.ErrorResponse;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.service.token.AccessTokenCreateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.gxdxx.instagram.config.jwt.TokenProvider.REFRESH_TOKEN;

@Tag(name = "tokens", description = "토큰 API")
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final AccessTokenCreateService accessTokenCreateService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "액세스 토큰 생성 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "액세스 토큰 생성 메소드", description = "액세스 토큰 생성 메소드입니다. 쿠키에 리프레시 토큰, 헤더에 액세스 토큰을 넣어주세요.")
    @PostMapping(value = "/api/token", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse createAccessToken(
            @CookieValue(REFRESH_TOKEN) Cookie cookie,
            @RequestHeader(TokenProvider.HEADER_AUTHORIZATION) String authorizationHeader,
            @RequestBody @Valid AccessTokenCreateRequest request,
            HttpServletResponse response
    ) {
        return accessTokenCreateService.createAccessToken(cookie.getValue(), authorizationHeader, request, response);
    }

}
