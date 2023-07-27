package com.gxdxx.instagram.domain.post.api;

import com.gxdxx.instagram.domain.post.application.PostUpdateService;
import com.gxdxx.instagram.domain.post.dto.request.PostUpdateRequest;
import com.gxdxx.instagram.domain.post.dto.response.PostUpdateResponse;
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

@Tag(name = "posts", description = "게시글 API")
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostUpdateController {

    private final PostUpdateService postUpdateService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                    content = @Content(schema = @Schema(implementation = PostUpdateResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "게시글 수정 메소드", description = "게시글 수정 메소드입니다.")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostUpdateResponse updatePost(
            @Valid PostUpdateRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        validatePostImage(request.image());
        return postUpdateService.updatePost(request, principal.getName());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

    private void validatePostImage(MultipartFile postImage) {
        if (postImage.isEmpty()) {
            throw new InvalidRequestException();
        }
    }

}
