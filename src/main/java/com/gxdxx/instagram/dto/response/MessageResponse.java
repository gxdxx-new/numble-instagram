package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MessageResponse(

        @Schema(description = "다음 커서")
        @JsonProperty("next_cursor")
        Long nextCursor,

        @Schema(description = "메시지 목록")
        List<MessageListResponse> messages

) {

    public static MessageResponse of(Long nextCursor, List<MessageListResponse> messages) {
        return new MessageResponse(nextCursor, messages);
    }

}
