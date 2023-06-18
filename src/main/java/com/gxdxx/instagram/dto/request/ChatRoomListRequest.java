package com.gxdxx.instagram.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatRoomListRequest(

        @Schema(description = "커서 (채팅방 id)")
        Long cursor

) {
}
