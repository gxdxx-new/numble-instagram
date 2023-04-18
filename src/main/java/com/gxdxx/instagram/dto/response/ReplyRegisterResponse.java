package com.gxdxx.instagram.dto.response;

import com.gxdxx.instagram.entity.Reply;

public record ReplyRegisterResponse(
        Long id,
        String content
) {

    public static ReplyRegisterResponse of(Reply reply) {
        return new ReplyRegisterResponse(reply.getId(), reply.getContent());
    }

}
