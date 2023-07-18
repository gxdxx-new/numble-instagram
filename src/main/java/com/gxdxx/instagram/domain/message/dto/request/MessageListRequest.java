package com.gxdxx.instagram.domain.message.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MessageListRequest(

        @Schema(description = "채팅방 id")
        @JsonProperty("chat_room_id")
        @Positive
        @NotNull
        Long chatRoomId,

        @Schema(description = "커서 (메시지 id)")
        Long cursor

) {
}
