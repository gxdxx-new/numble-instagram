package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageListResponse {

    @Schema(description = "메시지 id")
    @JsonProperty("message_id")
    public Long messageId;

    @Schema(description = "닉네임")
    public String nickname;

    @Schema(description = "프로필 사진 url")
    @JsonProperty("profile_image_url")
    public String profileImageUrl;

    @Schema(description = "메시지 내용")
    public String content;

    @Schema(description = "메시지 전송 시각")
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
