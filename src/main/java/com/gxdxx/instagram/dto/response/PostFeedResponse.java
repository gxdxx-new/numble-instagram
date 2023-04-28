package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostFeedResponse {

    @JsonProperty("post_id")
    public Long postId;

    @JsonProperty("content")
    public String content;

    @JsonProperty("image_url")
    public String imageUrl;

    @JsonProperty("user_id")
    public Long userId;

    @JsonProperty("nickname")
    public String nickname;

    @QueryProjection
    public PostFeedResponse(Long postId, String content, String imageUrl, Long userId, String nickname) {
        this.postId = postId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.nickname = nickname;
    }

}
