package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gxdxx.instagram.domain.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

public record PostRegisterResponse(

        @Schema(description = "게시글 id")
        Long id,

        @Schema(description = "게시글 내용")
        String content,

        @Schema(description = "사진 url")
        @JsonProperty("image_url")
        String imageUrl

) {

    public static PostRegisterResponse of(Post post) {
        return new PostRegisterResponse(post.getId(), post.getContent(), post.getImageUrl());
    }

}
