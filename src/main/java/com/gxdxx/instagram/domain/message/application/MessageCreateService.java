package com.gxdxx.instagram.domain.message.application;

import com.gxdxx.instagram.dto.request.MessageSendRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.chatroom.domain.ChatRoom;
import com.gxdxx.instagram.domain.message.domain.Message;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.chatroom.dao.ChatRoomRepository;
import com.gxdxx.instagram.domain.message.dao.MessageRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class MessageCreateService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public SuccessResponse sendMessage(MessageSendRequest request, String nickname) {
        User sendUser = findUserByNickname(nickname);
        User receiveUser = findUserById(request.userId());
        checkSendMessageToSelf(sendUser, receiveUser);
        ChatRoom chatRoom = findOrElseCreateChatRoom(sendUser, receiveUser);
        LocalDateTime now = LocalDateTime.now();
        chatRoom.updateLastMessage(request.content(), LocalDateTime.now());
        Message messageToSend = Message.of(request.content(), chatRoom, sendUser, receiveUser, now);
        messageRepository.save(messageToSend);
        return SuccessResponse.of("200 SUCCESS");
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private void checkSendMessageToSelf(User sendUser, User receiveUser) {
        if (sendUser.equals(receiveUser)) {
            throw new InvalidRequestException();
        }
    }

    private ChatRoom findOrElseCreateChatRoom(User sendUser, User receiveUser) {
        return chatRoomRepository.findByUserIds(sendUser.getId(), receiveUser.getId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.of(sendUser, receiveUser)));
    }

}
