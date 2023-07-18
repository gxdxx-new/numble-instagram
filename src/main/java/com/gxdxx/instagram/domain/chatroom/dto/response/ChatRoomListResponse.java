package com.gxdxx.instagram.domain.chatroom.dto.response;

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
public class ChatRoomListResponse {

    @Schema(description = "채팅방 id")
    @JsonProperty("chat_room_id")
    public Long chatRoomId;

    @Schema(description = "닉네임")
    public String nickname;

    @Schema(description = "프로필 사진 url")
    @JsonProperty("profile_image_url")
    public String profileImageUrl;

    @Schema(description = "마지막 메시지")
    @JsonProperty("last_message")
    public String lastMessage;

    @Schema(description = "마지막 전송시간")
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
