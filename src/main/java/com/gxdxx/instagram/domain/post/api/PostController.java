package com.gxdxx.instagram.domain.post.api;

import com.gxdxx.instagram.dto.request.PostFeedRequest;
import com.gxdxx.instagram.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.dto.request.PostUpdateRequest;
import com.gxdxx.instagram.dto.response.*;
import com.gxdxx.instagram.global.error.InvalidRequestException;
import com.gxdxx.instagram.domain.post.application.PostCreateService;
import com.gxdxx.instagram.domain.post.application.PostDeleteService;
import com.gxdxx.instagram.domain.post.application.PostFeedQueryService;
import com.gxdxx.instagram.domain.post.application.PostUpdateService;
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
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Tag(name = "posts", description = "게시글 API")
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostCreateService postCreateService;
    private final PostUpdateService postUpdateService;
    private final PostDeleteService postDeleteService;
    private final PostFeedQueryService postFeedQueryService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공",
                    content = @Content(schema = @Schema(implementation = PostRegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "게시글 작성 메소드", description = "게시글 작성 메소드입니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PostRegisterResponse createPost(
            @Valid PostRegisterRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        validatePostImage(request.image());
        return postCreateService.createPost(request, principal.getName());
    }

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "게시글 삭제 메소드", description = "게시글 삭제 메소드입니다.")
    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse deletePost(
            @Parameter(name = "id", description = "게시글의 id", in = ParameterIn.PATH) @PathVariable("id") Long id,
            Principal principal
    ) {
        return postDeleteService.deletePost(id, principal.getName());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 조회 성공",
                    content = @Content(schema = @Schema(implementation = FeedResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "피드 조회 메소드", description = "피드 조회 메소드입니다.")
    @GetMapping(value = "/{id}/feed", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FeedResponse findPostFeed(
            @RequestBody @Valid PostFeedRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return postFeedQueryService.findPostFeed(request, principal.getName());
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
