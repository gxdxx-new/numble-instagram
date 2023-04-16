package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gxdxx.instagram.entity.Post;

public record PostUpdateResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("content") String content,
        @JsonProperty("image_url") String imageUrl
) {

    public static PostUpdateResponse of(Post post) {
        return new PostUpdateResponse(post.getId(), post.getContent(), post.getImageUrl());
    }

}
