package com.gxdxx.instagram.dto.response;

import com.gxdxx.instagram.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommentRegisterResponse(

        @Schema(description = "댓글 id")
        Long id,

        @Schema(description = "댓글 내용")
        String content

) {

    public static CommentRegisterResponse of(Comment comment) {
        return new CommentRegisterResponse(comment.getId(), comment.getContent());
    }

}
