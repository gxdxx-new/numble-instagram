package com.gxdxx.instagram.domain.post.api;

import com.gxdxx.instagram.domain.post.application.PostFeedQueryService;
import com.gxdxx.instagram.domain.post.dto.request.PostFeedRequest;
import com.gxdxx.instagram.domain.post.dto.response.FeedResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "posts", description = "게시글 API")
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostFeedQueryController {

    private final PostFeedQueryService postFeedQueryService;

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

}
