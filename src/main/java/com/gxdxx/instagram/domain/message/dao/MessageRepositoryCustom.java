package com.gxdxx.instagram.domain.message.dao;

import com.gxdxx.instagram.domain.message.dto.response.MessageListResponse;

import java.util.List;

public interface MessageRepositoryCustom {

    List<MessageListResponse> getMessagesByCursor(Long requestingUserId, Long chatRoomId, Long cursor, int limit);

}
