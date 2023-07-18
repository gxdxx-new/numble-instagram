package com.gxdxx.instagram.domain.chatroom.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ChatRoomResponse(

        @Schema(description = "다음 커서")
        @JsonProperty("next_cursor")
        Long nextCursor,

        @Schema(description = "채팅방 목록")
        @JsonProperty("chat_rooms")
        List<ChatRoomListResponse> chatRooms

) {

    public static ChatRoomResponse of(Long nextCursor, List<ChatRoomListResponse> chatRooms) {
        return new ChatRoomResponse(nextCursor, chatRooms);
    }

}
