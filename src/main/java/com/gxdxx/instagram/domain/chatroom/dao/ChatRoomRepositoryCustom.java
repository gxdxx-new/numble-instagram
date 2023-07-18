package com.gxdxx.instagram.domain.chatroom.dao;

import com.gxdxx.instagram.domain.chatroom.dto.response.ChatRoomListResponse;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoomListResponse> getChatRoomsByCursor(Long requestingUserId, Long cursor, int limit);

}
