package com.gxdxx.instagram.domain.user.api;

import com.gxdxx.instagram.config.jwt.TokenProvider;
import com.gxdxx.instagram.domain.user.application.*;
import com.gxdxx.instagram.domain.user.dto.request.UserDeleteRequest;
import com.gxdxx.instagram.domain.user.dto.request.UserLoginRequest;
import com.gxdxx.instagram.domain.user.dto.request.UserProfileUpdateRequest;
import com.gxdxx.instagram.domain.user.dto.request.UserSignUpRequest;
import com.gxdxx.instagram.domain.user.dto.response.UserProfileResponse;
import com.gxdxx.instagram.domain.user.dto.response.UserProfileUpdateResponse;
import com.gxdxx.instagram.domain.user.dto.response.UserSignUpResponse;
import com.gxdxx.instagram.global.dto.response.ErrorResponse;
import com.gxdxx.instagram.global.dto.response.SuccessResponse;
import com.gxdxx.instagram.global.error.InvalidRequestException;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Tag(name = "users", description = "회원 API")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserCreateService userCreateService;
    private final UserProfileQueryService userProfileQueryService;
    private final UserProfileUpdateService userProfileUpdateService;
    private final UserDeleteService userDeleteService;
    private final UserLoginService userLoginService;
    private final UserLogoutService userLogoutService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = UserSignUpResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "회원가입 메소드", description = "회원가입 메소드입니다.")
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserSignUpResponse createUser(
            @Valid UserSignUpRequest request,
            BindingResult bindingResult
    ) {
        validateRequest(bindingResult);
        validateProfileImage(request.profileImage());
        return userCreateService.createUser(request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원탈퇴 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "회원탈퇴 메소드", description = "회원탈퇴 메소드입니다.")
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse deleteUser(
            @Valid UserDeleteRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return userDeleteService.deleteUser(request.password(), principal.getName());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "로그인 메소드", description = "로그인 메소드입니다. 토큰이 발급됩니다.")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse login(
            @RequestBody @Valid UserLoginRequest request,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {
        validateRequest(bindingResult);
        return userLoginService.login(request, response);
    }

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공",
                    content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "프로필 수정 메소드", description = "프로필 수정 메소드입니다.")
    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfileUpdateResponse updateUserProfile(
            @Valid UserProfileUpdateRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        validateProfileImage(request.profileImage());
        return userProfileUpdateService.updateUserProfile(request, principal.getName());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

    private void validateProfileImage(MultipartFile profileImage) {
        if (profileImage.isEmpty()) {
            throw new InvalidRequestException();
        }
    }

}
