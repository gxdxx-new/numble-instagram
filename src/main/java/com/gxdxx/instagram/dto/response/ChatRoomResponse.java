package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatRoomResponse(

        @JsonProperty("next_cursor")
        Long nextCursor,

        @JsonProperty("chat_rooms")
        List<ChatRoomListResponse> chatRooms

) {

    public static ChatRoomResponse of(Long nextCursor, List<ChatRoomListResponse> chatRooms) {
        return new ChatRoomResponse(nextCursor, chatRooms);
    }

}
