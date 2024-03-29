package com.gxdxx.instagram.domain.message.application;

import com.gxdxx.instagram.domain.message.dto.request.MessageListRequest;
import com.gxdxx.instagram.domain.message.dto.response.MessageListResponse;
import com.gxdxx.instagram.domain.message.dto.response.MessageResponse;
import com.gxdxx.instagram.domain.chatroom.domain.ChatRoom;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.chatroom.exception.ChatRoomNotFoundException;
import com.gxdxx.instagram.global.auth.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.chatroom.dao.ChatRoomRepository;
import com.gxdxx.instagram.domain.message.dao.MessageRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class MessageQueryService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final int LIMIT_SIZE = 5;
    private final Long DEFAULT_CURSOR = 0L;

    @Transactional(readOnly = true)
    public MessageResponse findMessages(MessageListRequest request, String nickname) {
        User requestUser = findUserByNickname(nickname);
        ChatRoom chatRoom = findChatRoomByChatRoomId(request.chatRoomId());
        checkUserInChatRoom(requestUser, chatRoom);
        Long cursor = determineCursor(request.cursor());
        List<MessageListResponse> messages = messageRepository.getMessagesByCursor(requestUser.getId(), chatRoom.getId(), cursor, LIMIT_SIZE);
        Long nextCursor = determineNextCursor(messages);
        return MessageResponse.of(nextCursor, messages);
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private ChatRoom findChatRoomByChatRoomId(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);
    }

    private void checkUserInChatRoom(User user, ChatRoom chatRoom) {
        if (!chatRoom.hasUser(user)) {
            throw new UnauthorizedAccessException();
        }
    }

    private Long determineCursor(Long requestCursor) {
        if (requestCursor != null) {
            return requestCursor;
        }
        return findMaxMessageIdOrElseDefaultCursor();
    }

    private Long findMaxMessageIdOrElseDefaultCursor() {
        return messageRepository.findMaxMessageId()
                .map(maxId -> maxId + 1)
                .orElse(DEFAULT_CURSOR);
    }

    private Long determineNextCursor(List<MessageListResponse> messages) {
        return messages.stream()
                .map(MessageListResponse::getMessageId)
                .reduce((first, second) -> second)
                .orElse(DEFAULT_CURSOR);
    }

}
