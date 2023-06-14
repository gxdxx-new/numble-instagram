package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MessageListRequest(

        @JsonProperty("chat_room_id")
        @Positive
        @NotNull
        Long chatRoomId,

        Long cursor

) {
}
