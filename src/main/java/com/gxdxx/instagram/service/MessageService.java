package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.MessageSendRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.ChatRoom;
import com.gxdxx.instagram.entity.Message;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.ChatRoomRepository;
import com.gxdxx.instagram.repository.MessageRepository;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Message message = Message.of(request.content(), chatRoom, sendUser, receiveUser);
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

}
