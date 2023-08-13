package com.gxdxx.instagram.domain.message.application;

import com.gxdxx.instagram.domain.chatroom.dao.ChatRoomRepository;
import com.gxdxx.instagram.domain.chatroom.domain.ChatRoom;
import com.gxdxx.instagram.domain.message.dao.MessageRepository;
import com.gxdxx.instagram.domain.message.domain.Message;
import com.gxdxx.instagram.domain.message.dto.request.MessageSendRequest;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.global.error.InvalidRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageCreateServiceTest {

    @InjectMocks
    private MessageCreateService messageCreateService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private UserRepository userRepository;

    private static final Long FAKE_USER_ID = 1L;
    private static final String SENDER = "sender";
    private static final Long RECEIVER_ID = 1L;
    private static final String RECEIVER = "receiver";
    private static final String PASSWORD = "password";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String MESSAGE_CONTENT = "messageContent";
    private static final String SUCCESS_MESSAGE = "200 SUCCESS";

    @Test
    @DisplayName("[메시지 전송] - 성공 (기존 채팅방이 존재할 경우)")
    public void sendMessage_withAlreadyExistingChatRoom_shouldSucceed() {
        // given
        MessageSendRequest request = createMessageSendRequest();
        User sender = createSender();
        User receiver = createReceiver();
        ChatRoom chatRoom = createChatRoom(sender, receiver);
        Message message = createMessage(chatRoom, sender, receiver);

        when(userRepository.findByNickname(SENDER))
                .thenReturn(Optional.of(sender));
        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(receiver));
        when(chatRoomRepository.findByUserIds(any(), any()))
                .thenReturn(Optional.of(chatRoom));
        when(messageRepository.save(any(Message.class)))
                .thenReturn(message);

        // when
        SuccessResponse response = messageCreateService.sendMessage(request, SENDER);

        // then
        Assertions.assertEquals(request.content(), chatRoom.getLastMessage());
        Assertions.assertNotNull(chatRoom.getLastSentAt());
        Assertions.assertEquals(SUCCESS_MESSAGE, response.successMessage());
    }

    @Test
    @DisplayName("[메시지 전송] - 성공 (기존 채팅방이 존재하지 않을 경우)")
    public void sendMessage_withNonExistingChatRoom_shouldSucceed() {
        // given
        MessageSendRequest request = createMessageSendRequest();
        User sender = createSender();
        User receiver = createReceiver();
        ChatRoom chatRoom = createChatRoom(sender, receiver);
        Message message = createMessage(chatRoom, sender, receiver);

        when(userRepository.findByNickname(SENDER))
                .thenReturn(Optional.of(sender));
        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(receiver));
        when(chatRoomRepository.findByUserIds(any(), any()))
                .thenReturn(Optional.empty());
        when(chatRoomRepository.save(any(ChatRoom.class)))
                .thenReturn(chatRoom);
        when(messageRepository.save(any(Message.class)))
                .thenReturn(message);

        // when
        SuccessResponse response = messageCreateService.sendMessage(request, SENDER);

        // then
        Assertions.assertEquals(request.content(), chatRoom.getLastMessage());
        Assertions.assertNotNull(chatRoom.getLastSentAt());
        Assertions.assertEquals(SUCCESS_MESSAGE, response.successMessage());
    }

    @Test
    @DisplayName("[메시지 전송] - 실패 (해당 닉네임을 가진 유저가 존재하지 않는 경우)")
    public void sendMessage_withNonExistingNickname_UserNotFoundException() {
        // given
        MessageSendRequest request = createMessageSendRequest();

        when(userRepository.findByNickname(SENDER))
                .thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(UserNotFoundException.class,
                () -> messageCreateService.sendMessage(request, SENDER));
    }

    @Test
    @DisplayName("[메시지 전송] - 실패 (해당 id를 가진 유저가 존재하지 않는 경우)")
    public void sendMessage_withNonExistingId_UserNotFoundException() {
        // given
        MessageSendRequest request = createMessageSendRequest();
        User sender = createSender();

        when(userRepository.findByNickname(SENDER))
                .thenReturn(Optional.of(sender));
        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(UserNotFoundException.class,
                () -> messageCreateService.sendMessage(request, SENDER));
    }

    @Test
    @DisplayName("[메시지 전송] - 실패 (자기 자신에게 메시지를 전송할 경우)")
    public void sendMessage_withSameSenderAndReceiver_InvalidRequestException() {
        // given
        MessageSendRequest request = createMessageSendRequest();
        User sender = createSender();
        User receiver = createSender();
        ReflectionTestUtils.setField(sender, "id", FAKE_USER_ID);
        ReflectionTestUtils.setField(receiver, "id", FAKE_USER_ID);

        when(userRepository.findByNickname(SENDER))
                .thenReturn(Optional.of(sender));
        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(receiver));

        // when & then
        Assertions.assertThrows(InvalidRequestException.class,
                () -> messageCreateService.sendMessage(request, SENDER));
    }

    private MessageSendRequest createMessageSendRequest() {
        return new MessageSendRequest(RECEIVER_ID, MESSAGE_CONTENT);
    }

    private User createSender() {
        return User.of(SENDER, PASSWORD, PROFILE_IMAGE_URL);
    }

    private User createReceiver() {
        return User.of(RECEIVER, PASSWORD, PROFILE_IMAGE_URL);
    }

    private ChatRoom createChatRoom(User sender, User receiver) {
        return ChatRoom.of(sender, receiver);
    }

    private Message createMessage(ChatRoom chatRoom, User sender, User receiver) {
        return Message.of(MESSAGE_CONTENT, chatRoom, sender, receiver, now());
    }

}