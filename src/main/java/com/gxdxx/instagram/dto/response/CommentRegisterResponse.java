package com.gxdxx.instagram.dto.response;

import com.gxdxx.instagram.entity.Comment;

public record CommentRegisterResponse(
        Long id,
        String content
) {

    public static CommentRegisterResponse of(Comment comment) {
        return new CommentRegisterResponse(comment.getId(), comment.getContent());
    }

}
