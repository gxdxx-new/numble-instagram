package com.gxdxx.instagram.domain.comment.dto.response;

import com.gxdxx.instagram.domain.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommentUpdateResponse(

        @Schema(description = "댓글 id")
        Long id,

        @Schema(description = "댓글 내용")
        String content

) {

    public static CommentUpdateResponse of(Comment comment) {
        return new CommentUpdateResponse(comment.getId(), comment.getContent());
    }

}
