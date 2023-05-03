package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.dto.response.ChatRoomListResponse;
import com.gxdxx.instagram.dto.response.MessageListResponse;

import java.util.List;

public interface MessageRepositoryCustom {

    List<MessageListResponse> getMessagesByCursor(Long requestingUserId, Long chatRoomId, Long cursor, int limit);

}
