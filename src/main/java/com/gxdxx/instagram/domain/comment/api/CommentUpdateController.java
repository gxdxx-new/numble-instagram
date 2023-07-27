package com.gxdxx.instagram.domain.comment.api;

import com.gxdxx.instagram.domain.comment.application.CommentDeleteService;
import com.gxdxx.instagram.global.common.dto.response.ErrorResponse;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "comments", description = "댓글 API")
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@RestController
public class CommentUpdateController {

    private final CommentDeleteService commentDeleteService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "댓글 삭제 메소드", description = "댓글 삭제 메소드입니다.")
    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessResponse deleteComment(
            @Parameter(name = "id", description = "댓글의 id", in = ParameterIn.PATH) @PathVariable("id") Long id,
            Principal principal
    ) {
        return commentDeleteService.deleteComment(id, principal.getName());
    }

}
