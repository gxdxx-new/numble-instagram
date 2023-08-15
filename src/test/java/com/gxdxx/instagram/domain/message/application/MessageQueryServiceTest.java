package com.gxdxx.instagram.domain.message.application;

import com.gxdxx.instagram.domain.chatroom.dao.ChatRoomRepository;
import com.gxdxx.instagram.domain.chatroom.domain.ChatRoom;
import com.gxdxx.instagram.domain.chatroom.exception.ChatRoomNotFoundException;
import com.gxdxx.instagram.domain.message.dao.MessageRepository;
import com.gxdxx.instagram.domain.message.dto.request.MessageListRequest;
import com.gxdxx.instagram.domain.message.dto.response.MessageListResponse;
import com.gxdxx.instagram.domain.message.dto.response.MessageResponse;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageQueryServiceTest {

    @InjectMocks
    private MessageQueryService messageQueryService;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private UserRepository userRepository;


    private static final String REQUEST_USER_NICKNAME = "requestUserNickname";
    private static final String PARTICIPANT = "participant";
    private static final String PASSWORD = "password";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final Long CHATROOM_ID = 1L;
    private static final Long MAX_MESSAGE_ID = 10L;
    private static final int LIMIT = 5;
    private static final Long DEFAULT_CURSOR = 0L;
    private static final Long EXIST_CURSOR = 11L;
    private static final Long NEXT_CURSOR = 1L;
    private static final int EXIST_MESSAGE_SIZE = 5;
    private static final int EMPTY_MESSAGE_SIZE = 0;
    private static final String CONTENT = "content";
    private static final LocalDateTime SENT_AT = now();

    @Test
    @DisplayName("[메시지 조회] - 성공 (cursor가 null && message가 존재 && message list가 존재)")
    void findMessages_withNullCursorAndExistsMessagesAndMessageList_shouldSucceed() {
        // given
        MessageListRequest request = createMessageListRequest(null);
        User requestUser = createUser(REQUEST_USER_NICKNAME);
        User participant = createUser(PARTICIPANT);
        ChatRoom chatRoom = createChatRoom(requestUser, participant);

        List<MessageListResponse> messages = createMessageList();

        when(userRepository.findByNickname(REQUEST_USER_NICKNAME))
                .thenReturn(Optional.of(requestUser));
        when(chatRoomRepository.findById(CHATROOM_ID))
                .thenReturn(Optional.of(chatRoom));
        when(messageRepository.findMaxMessageId())
                .thenReturn(Optional.of(MAX_MESSAGE_ID));
        when(messageRepository.getMessagesByCursor(any(), any(), eq(MAX_MESSAGE_ID + 1), eq(LIMIT)))
                .thenReturn(messages);

        // when
        MessageResponse response = messageQueryService.findMessages(request, REQUEST_USER_NICKNAME);

        // then
        assertEquals(response.messages().size(), EXIST_MESSAGE_SIZE);
        assertEquals(response.nextCursor(), NEXT_CURSOR);
    }

    @Test
    @DisplayName("[메시지 조회] - 성공 (cursor가 null && message가 존재 && message list가 존재 x)")
    void findMessages_withNullCursorAndExistsMessagesAndNotExistsMessageList_shouldSucceed() {
        // given
        MessageListRequest request = createMessageListRequest(null);
        User requestUser = createUser(REQUEST_USER_NICKNAME);
        User participant = createUser(PARTICIPANT);
        ChatRoom chatRoom = createChatRoom(requestUser, participant);

        List<MessageListResponse> messages = new ArrayList<>();

        when(userRepository.findByNickname(REQUEST_USER_NICKNAME))
                .thenReturn(Optional.of(requestUser));
        when(chatRoomRepository.findById(CHATROOM_ID))
                .thenReturn(Optional.of(chatRoom));
        when(messageRepository.findMaxMessageId())
                .thenReturn(Optional.of(MAX_MESSAGE_ID));
        when(messageRepository.getMessagesByCursor(any(), any(), eq(MAX_MESSAGE_ID + 1), eq(LIMIT)))
                .thenReturn(messages);

        // when
        MessageResponse response = messageQueryService.findMessages(request, REQUEST_USER_NICKNAME);

        // then
        assertEquals(response.messages().size(), EMPTY_MESSAGE_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[메시지 조회] - 성공 (cursor가 null && message가 존재 x && message list가 존재 x)")
    void findMessages_withNullCursorAndNotExistsMessagesAndMessageList_shouldSucceed() {
        // given
        MessageListRequest request = createMessageListRequest(null);
        User requestUser = createUser(REQUEST_USER_NICKNAME);
        User participant = createUser(PARTICIPANT);
        ChatRoom chatRoom = createChatRoom(requestUser, participant);

        List<MessageListResponse> messages = new ArrayList<>();

        when(userRepository.findByNickname(REQUEST_USER_NICKNAME))
                .thenReturn(Optional.of(requestUser));
        when(chatRoomRepository.findById(CHATROOM_ID))
                .thenReturn(Optional.of(chatRoom));
        when(messageRepository.findMaxMessageId())
                .thenReturn(Optional.of(DEFAULT_CURSOR));
        when(messageRepository.getMessagesByCursor(any(), any(), eq(DEFAULT_CURSOR + 1), eq(LIMIT)))
                .thenReturn(messages);

        // when
        MessageResponse response = messageQueryService.findMessages(request, REQUEST_USER_NICKNAME);

        // then
        assertEquals(response.messages().size(), EMPTY_MESSAGE_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[메시지 조회] - 성공 (cursor가 존재 && message list가 존재)")
    void findMessages_withCursorAndExistsMessageList_shouldSucceed() {
        // given
        MessageListRequest request = createMessageListRequest(EXIST_CURSOR);
        User requestUser = createUser(REQUEST_USER_NICKNAME);
        User participant = createUser(PARTICIPANT);
        ChatRoom chatRoom = createChatRoom(requestUser, participant);

        List<MessageListResponse> messages = createMessageList();

        when(userRepository.findByNickname(REQUEST_USER_NICKNAME))
                .thenReturn(Optional.of(requestUser));
        when(chatRoomRepository.findById(CHATROOM_ID))
                .thenReturn(Optional.of(chatRoom));
        when(messageRepository.getMessagesByCursor(any(), any(), eq(EXIST_CURSOR), eq(LIMIT)))
                .thenReturn(messages);

        // when
        MessageResponse response = messageQueryService.findMessages(request, REQUEST_USER_NICKNAME);

        // then
        assertEquals(response.messages().size(), EXIST_MESSAGE_SIZE);
        assertEquals(response.nextCursor(), NEXT_CURSOR);
    }

    @Test
    @DisplayName("[메시지 조회] - 성공 (cursor가 존재 && message list가 존재 x)")
    void findMessages_withCursorAndNotExistsMessageList_shouldSucceed() {
        // given
        MessageListRequest request = createMessageListRequest(EXIST_CURSOR);
        User requestUser = createUser(REQUEST_USER_NICKNAME);
        User participant = createUser(PARTICIPANT);
        ChatRoom chatRoom = createChatRoom(requestUser, participant);

        List<MessageListResponse> messages = new ArrayList<>();

        when(userRepository.findByNickname(REQUEST_USER_NICKNAME))
                .thenReturn(Optional.of(requestUser));
        when(chatRoomRepository.findById(CHATROOM_ID))
                .thenReturn(Optional.of(chatRoom));
        when(messageRepository.getMessagesByCursor(any(), any(), eq(EXIST_CURSOR), eq(LIMIT)))
                .thenReturn(messages);

        // when
        MessageResponse response = messageQueryService.findMessages(request, REQUEST_USER_NICKNAME);

        // then
        assertEquals(response.messages().size(), EMPTY_MESSAGE_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[메시지 조회] - 실패 (존재하지 않는 회원)")
    void findMessages_withNonExistingUser_UserNotFoundException() {
        // given
        MessageListRequest request = createMessageListRequest(null);

        when(userRepository.findByNickname(REQUEST_USER_NICKNAME))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> messageQueryService.findMessages(request, REQUEST_USER_NICKNAME));
    }

    @Test
    @DisplayName("[메시지 조회] - 실패 (존재하지 않는 회원)")
    void findMessages_withNonExistingChatRoom_ChatRoomNotFoundException() {
        // given
        MessageListRequest request = createMessageListRequest(null);
        User requestUser = createUser(REQUEST_USER_NICKNAME);

        when(userRepository.findByNickname(REQUEST_USER_NICKNAME))
                .thenReturn(Optional.of(requestUser));
        when(chatRoomRepository.findById(request.chatRoomId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ChatRoomNotFoundException.class, () -> messageQueryService.findMessages(request, REQUEST_USER_NICKNAME));
    }

    private MessageListRequest createMessageListRequest(Long cursor) {
        return new MessageListRequest(CHATROOM_ID, cursor);
    }

    private User createUser(String nickname) {
        return User.of(nickname, PASSWORD, PROFILE_IMAGE_URL);
    }

    private ChatRoom createChatRoom(User UserA, User UserB) {
        return ChatRoom.of(UserA, UserB);
    }

    private List<MessageListResponse> createMessageList() {
        List<MessageListResponse> messages = new ArrayList<>();
        for (int messageId = EXIST_MESSAGE_SIZE; messageId > 0; messageId--) {
            messages.add(new MessageListResponse(
                            (long) messageId,
                            PARTICIPANT + messageId,
                            PROFILE_IMAGE_URL + messageId,
                            CONTENT + messageId,
                            SENT_AT
                    )
            );
        }
        return messages;
    }

}