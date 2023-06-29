package com.gxdxx.instagram.service.chatroom;

import com.gxdxx.instagram.dto.request.ChatRoomListRequest;
import com.gxdxx.instagram.dto.response.ChatRoomListResponse;
import com.gxdxx.instagram.dto.response.ChatRoomResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.ChatRoomRepository;
import com.gxdxx.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ChatRoomResponse findChatRooms(ChatRoomListRequest request, String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
        Long cursor = (request.cursor() == null)
                ? chatRoomRepository.findMaxChatRoomId().map(maxId -> maxId + 1).orElse(0L)
                : request.cursor();
        List<ChatRoomListResponse> chatRooms = chatRoomRepository.getChatRoomsByCursor(user.getId(), cursor, 5);
        Long nextCursor = !chatRooms.isEmpty() ? chatRooms.get(chatRooms.size() - 1).getChatRoomId() : 0L;

        return ChatRoomResponse.of(nextCursor, chatRooms);
    }

}
