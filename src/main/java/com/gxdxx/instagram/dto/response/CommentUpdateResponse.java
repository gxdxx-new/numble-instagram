package com.gxdxx.instagram.dto.response;

import com.gxdxx.instagram.entity.Comment;

public record CommentUpdateResponse(
        Long id,
        String content
) {

    public static CommentUpdateResponse of(Comment comment) {
        return new CommentUpdateResponse(comment.getId(), comment.getContent());
    }

}
