package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gxdxx.instagram.entity.Post;

public record PostRegisterResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("content") String content,
        @JsonProperty("image_url") String imageUrl) {

    public static PostRegisterResponse of(Post post) {
        return new PostRegisterResponse(post.getId(), post.getContent(), post.getImageUrl());
    }

}
