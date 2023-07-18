package com.gxdxx.instagram.domain.reply.dto.response;

import com.gxdxx.instagram.domain.reply.domain.Reply;
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
