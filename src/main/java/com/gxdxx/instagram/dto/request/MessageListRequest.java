package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;

public record MessageListRequest(
        @JsonProperty("chat_room_id") @Positive Long chatRoomId,
        Long cursor
) {
}
