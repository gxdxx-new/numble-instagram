package com.gxdxx.instagram.dto.response;

import com.gxdxx.instagram.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReplyUpdateResponse(

        @Schema(description = "답글 id")
        Long id,

        @Schema(description = "답글 내용")
        String content

) {

    public static ReplyUpdateResponse of(Reply reply) {
        return new ReplyUpdateResponse(reply.getId(), reply.getContent());
    }

}
