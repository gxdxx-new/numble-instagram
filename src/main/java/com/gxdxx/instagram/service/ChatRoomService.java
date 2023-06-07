package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.ChatRoomListRequest;
import com.gxdxx.instagram.dto.response.ChatRoomListResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.ChatRoomRepository;
import com.gxdxx.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getChatRooms(ChatRoomListRequest request, String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
        Long cursor = (request.cursor() == null)
                ? chatRoomRepository.findMaxChatRoomId().map(maxId -> maxId + 1).orElse(0L)
                : request.cursor();
        List<ChatRoomListResponse> list = chatRoomRepository.getChatRoomsByCursor(user.getId(), cursor, 5);
        Long nextCursor = !list.isEmpty() ? list.get(list.size() - 1).getChatRoomId() : 0L;
        Map<String, Object> response = new HashMap<>();
        response.put("cursor", nextCursor);
        response.put("chat_rooms", list);

        return response;
    }

}
