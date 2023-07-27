package com.gxdxx.instagram.domain.user.api;

import com.gxdxx.instagram.domain.user.application.UserProfileUpdateService;
import com.gxdxx.instagram.domain.user.dto.request.UserProfileUpdateRequest;
import com.gxdxx.instagram.domain.user.dto.response.UserProfileResponse;
import com.gxdxx.instagram.domain.user.dto.response.UserProfileUpdateResponse;
import com.gxdxx.instagram.global.common.dto.response.ErrorResponse;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Tag(name = "users", description = "회원 API")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserProfileUpdateController {

    private final UserProfileUpdateService userProfileUpdateService;

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
