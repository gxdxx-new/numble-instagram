package com.gxdxx.instagram.dto.response;

import com.gxdxx.instagram.entity.Reply;

public record ReplyUpdateResponse(
        Long id,
        String content
) {

    public static ReplyUpdateResponse of(Reply reply) {
        return new ReplyUpdateResponse(reply.getId(), reply.getContent());
    }

}
