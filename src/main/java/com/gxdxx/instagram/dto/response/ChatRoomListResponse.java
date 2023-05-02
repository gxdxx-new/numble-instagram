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
public class ChatRoomListResponse {

    @JsonProperty("chat_room_id")
    public Long chatRoomId;

    @JsonProperty("nickname")
    public String nickname;

    @JsonProperty("profile_image_url")
    public String profileImageUrl;

    @JsonProperty("last_message")
    public String lastMessage;

    @JsonProperty("last_sent_at")
    public LocalDateTime lastSentAt;


    @QueryProjection
    public ChatRoomListResponse(Long chatRoomId, String nickname, String profileImageUrl, String lastMessage, LocalDateTime lastSentAt) {
        this.chatRoomId = chatRoomId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.lastMessage = lastMessage;
        this.lastSentAt = lastSentAt;
    }

}
