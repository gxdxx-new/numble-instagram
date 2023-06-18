package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MessageResponse(

        @JsonProperty("next_cursor")
        Long nextCursor,

        List<MessageListResponse> messages

) {

    public static MessageResponse of(Long nextCursor, List<MessageListResponse> messages) {
        return new MessageResponse(nextCursor, messages);
    }

}
