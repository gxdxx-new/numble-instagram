package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostFeedResponse {

    @Schema(description = "게시글 id")
    @JsonProperty("post_id")
    public Long postId;

    @Schema(description = "게시글 내용")
    public String content;

    @Schema(description = "사진 url")
    @JsonProperty("image_url")
    public String imageUrl;

    @Schema(description = "작성자 id")
    @JsonProperty("user_id")
    public Long userId;

    @Schema(description = "작성자 닉네임")
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
