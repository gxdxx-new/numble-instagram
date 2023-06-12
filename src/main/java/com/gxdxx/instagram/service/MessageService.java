package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.MessageListRequest;
import com.gxdxx.instagram.dto.request.MessageSendRequest;
import com.gxdxx.instagram.dto.response.MessageListResponse;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.ChatRoom;
import com.gxdxx.instagram.entity.Message;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.ChatRoomNotFoundException;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.ChatRoomRepository;
import com.gxdxx.instagram.repository.MessageRepository;
import com.gxdxx.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public SuccessResponse sendMessage(MessageSendRequest request, String nickname) {
        User sendUser = getUserByNickname(nickname);
        User receiveUser = getUserById(request.userId());
        validateUsers(sendUser, receiveUser);
        ChatRoom chatRoom = getOrCreateChatRoom(sendUser, receiveUser);
        LocalDateTime now = LocalDateTime.now();
        chatRoom.updateLastMessage(request.content(), now);
        Message message = Message.of(request.content(), chatRoom, sendUser, receiveUser, now);
        messageRepository.save(message);
        return SuccessResponse.of("200 SUCCESS");
    }

    private User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateUsers(User sendUser, User receiveUser) {
        if (sendUser.equals(receiveUser)) {
            throw new InvalidRequestException();
        }
    }

    private ChatRoom getOrCreateChatRoom(User sendUser, User receiveUser) {
        return chatRoomRepository.findByUserIds(sendUser.getId(), receiveUser.getId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.of(sendUser, receiveUser)));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMessages(MessageListRequest request, String nickname) {
        User requestUser = getUserByNickname(nickname);
        ChatRoom chatRoom = chatRoomRepository.findById(request.chatRoomId())
                .orElseThrow(ChatRoomNotFoundException::new);
        if (!chatRoom.hasUser(requestUser)) {
            throw new UnauthorizedAccessException();
        }
        Long cursor = (request.cursor() == null)
                ? messageRepository.findMaxMessageId().map(maxId -> maxId + 1).orElse(0L)
                : request.cursor();
        List<MessageListResponse> messages = messageRepository.getMessagesByCursor(requestUser.getId(), chatRoom.getId(), cursor, 5);
        Long nextCursor = messages.isEmpty() ? 0L : messages.get(messages.size() - 1).getMessageId();

        return Map.of("cursor", nextCursor, "messages", messages);
    }

}
