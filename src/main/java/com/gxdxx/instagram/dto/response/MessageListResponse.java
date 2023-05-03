package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageListResponse {

    @JsonProperty("message_id")
    public Long messageId;

    @JsonProperty("nickname")
    public String nickname;

    @JsonProperty("profile_image_url")
    public String profileImageUrl;

    @JsonProperty("content")
    public String content;

    @JsonProperty("sent_at")
    public LocalDateTime sentAt;


    @QueryProjection
    public MessageListResponse(Long messageId, String nickname, String profileImageUrl, String content, LocalDateTime sentAt) {
        this.messageId = messageId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.sentAt = sentAt;
    }

}
