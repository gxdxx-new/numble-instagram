package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.dto.response.ChatRoomListResponse;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoomListResponse> getChatRoomsByCursor(Long requestingUserId, Long cursor, int limit);

}
