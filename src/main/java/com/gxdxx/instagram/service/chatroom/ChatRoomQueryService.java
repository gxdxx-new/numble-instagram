package com.gxdxx.instagram.service.chatroom;

import com.gxdxx.instagram.dto.request.ChatRoomListRequest;
import com.gxdxx.instagram.dto.response.ChatRoomListResponse;
import com.gxdxx.instagram.dto.response.ChatRoomResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.chatroom.dao.ChatRoomRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
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
    private final int LIMIT_SIZE = 5;
    private final Long DEFAULT_CURSOR = 0L;

    @Transactional(readOnly = true)
    public ChatRoomResponse findChatRooms(ChatRoomListRequest request, String nickname) {
        User chatRoomUser = findUserByNickname(nickname);
        Long cursor = determineCursor(request.cursor());
        List<ChatRoomListResponse> chatRooms = chatRoomRepository.getChatRoomsByCursor(chatRoomUser.getId(), cursor, LIMIT_SIZE);
        Long nextCursor = determineNextCursor(chatRooms);
        return ChatRoomResponse.of(nextCursor, chatRooms);
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private Long determineCursor(Long requestCursor) {
        if (requestCursor != null) {
            return requestCursor;
        }
        return findMaxChatRoomIdOrElseDefaultCursor();
    }

    private Long findMaxChatRoomIdOrElseDefaultCursor() {
        return chatRoomRepository.findMaxChatRoomId()
                .map(maxId -> maxId + 1)
                .orElse(DEFAULT_CURSOR);
    }

    private Long determineNextCursor(List<ChatRoomListResponse> chatRooms) {
        return chatRooms.stream()
                .map(ChatRoomListResponse::getChatRoomId)
                .reduce((first, second) -> second)
                .orElse(DEFAULT_CURSOR);
    }

}
