package com.gxdxx.instagram.domain.chatroom.application;

import com.gxdxx.instagram.domain.chatroom.dao.ChatRoomRepository;
import com.gxdxx.instagram.domain.chatroom.dto.request.ChatRoomListRequest;
import com.gxdxx.instagram.domain.chatroom.dto.response.ChatRoomListResponse;
import com.gxdxx.instagram.domain.chatroom.dto.response.ChatRoomResponse;
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
class ChatRoomQueryServiceTest {

    @InjectMocks
    private ChatRoomQueryService chatRoomQueryService;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private UserRepository userRepository;

    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final Long MAX_CHATROOM_ID = 10L;
    private static final int LIMIT = 5;
    private static final Long DEFAULT_CURSOR = 0L;
    private static final Long EXIST_CURSOR = 11L;
    private static final Long NEXT_CURSOR = 1L;
    private static final int EXIST_CHATROOM_SIZE = 5;
    private static final int EMPTY_CHATROOM_SIZE = 0;
    private static final String LAST_MESSAGE = "lastMessage";
    private static final LocalDateTime LAST_SENT_AT  = now();

    @Test
    @DisplayName("[채팅방 조회] - 성공 (cursor가 null && chatroom이 존재 && chatroom list가 존재)")
    void findChatRooms_withNullCursorAndExistsChatRoomsAndChatRoomList_shouldSucceed() {
        // given
        ChatRoomListRequest request = createChatRoomListRequest(null);
        User user = createUser();

        List<ChatRoomListResponse> chatRooms = createChatRoomList();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findMaxChatRoomId()).thenReturn(Optional.of(MAX_CHATROOM_ID));
        when(chatRoomRepository.getChatRoomsByCursor(any(), eq(MAX_CHATROOM_ID + 1), eq(LIMIT))).thenReturn(chatRooms);

        // when
        ChatRoomResponse response = chatRoomQueryService.findChatRooms(request, NICKNAME);

        // then
        assertEquals(response.chatRooms().size(), EXIST_CHATROOM_SIZE);
        assertEquals(response.nextCursor(), NEXT_CURSOR);
    }

    @Test
    @DisplayName("[채팅방 조회] - 성공 (cursor가 null && chatroom이 존재 && chatroom list가 존재 x)")
    void findChatRooms_withNullCursorAndExistsChatRoomsAndNotExistsChatRoomList_shouldSucceed() {
        // given
        ChatRoomListRequest request = createChatRoomListRequest(null);
        User user = createUser();

        List<ChatRoomListResponse> chatRooms = new ArrayList<>();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findMaxChatRoomId()).thenReturn(Optional.of(MAX_CHATROOM_ID));
        when(chatRoomRepository.getChatRoomsByCursor(any(), eq(MAX_CHATROOM_ID + 1), eq(LIMIT))).thenReturn(chatRooms);

        // when
        ChatRoomResponse response = chatRoomQueryService.findChatRooms(request, NICKNAME);

        // then
        assertEquals(response.chatRooms().size(), EMPTY_CHATROOM_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[채팅방 조회] - 성공 (cursor가 null && chatroom이 존재 x && chatroom list가 존재 x)")
    void findChatRooms_withNullCursorAndNotExistsChatRoomsAndChatRoomList_shouldSucceed() {
        // given
        ChatRoomListRequest request = createChatRoomListRequest(null);
        User user = createUser();

        List<ChatRoomListResponse> chatRooms = new ArrayList<>();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findMaxChatRoomId()).thenReturn(Optional.of(DEFAULT_CURSOR));
        when(chatRoomRepository.getChatRoomsByCursor(any(), eq(DEFAULT_CURSOR + 1), eq(LIMIT))).thenReturn(chatRooms);

        // when
        ChatRoomResponse response = chatRoomQueryService.findChatRooms(request, NICKNAME);

        // then
        assertEquals(response.chatRooms().size(), EMPTY_CHATROOM_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[채팅방 조회] - 성공 (cursor가 존재 && chatroom list가 존재)")
    void findChatRooms_withCursorAndExistsChatRomList_shouldSucceed() {
        // given
        ChatRoomListRequest request = createChatRoomListRequest(EXIST_CURSOR);
        User user = createUser();

        List<ChatRoomListResponse> chatRooms = createChatRoomList();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(chatRoomRepository.getChatRoomsByCursor(any(), eq(EXIST_CURSOR), eq(LIMIT))).thenReturn(chatRooms);

        // when
        ChatRoomResponse response = chatRoomQueryService.findChatRooms(request, NICKNAME);

        // then
        assertEquals(response.chatRooms().size(), EXIST_CHATROOM_SIZE);
        assertEquals(response.nextCursor(), NEXT_CURSOR);
    }

    @Test
    @DisplayName("[채팅방 조회] - 성공 (cursor가 존재 && chatroom list가 존재 x)")
    void findChatRooms_withCursorAndNotExistsChatRoomList_shouldSucceed() {
        // given
        ChatRoomListRequest request = createChatRoomListRequest(EXIST_CURSOR);
        User user = createUser();

        List<ChatRoomListResponse> chatRooms = new ArrayList<>();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(chatRoomRepository.getChatRoomsByCursor(any(), eq(EXIST_CURSOR), eq(LIMIT))).thenReturn(chatRooms);

        // when
        ChatRoomResponse response = chatRoomQueryService.findChatRooms(request, NICKNAME);

        // then
        assertEquals(response.chatRooms().size(), EMPTY_CHATROOM_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[채팅방 조회] - 실패 (존재하지 않는 회원)")
    void findChatRooms_withNonExistingUser_UserNotFoundException() {
        // given
        ChatRoomListRequest request = createChatRoomListRequest(null);

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> chatRoomQueryService.findChatRooms(request, NICKNAME));
    }

    private ChatRoomListRequest createChatRoomListRequest(Long cursor) {
        return new ChatRoomListRequest(cursor);
    }

    private User createUser() {
        return User.of(NICKNAME, PASSWORD, PROFILE_IMAGE_URL);
    }

    private List<ChatRoomListResponse> createChatRoomList() {
        List<ChatRoomListResponse> chatRooms = new ArrayList<>();
        for (int chatRoomId = EXIST_CHATROOM_SIZE; chatRoomId > 0; chatRoomId--) {
            chatRooms.add(new ChatRoomListResponse(
                    (long) chatRoomId,
                    NICKNAME + chatRoomId,
                    PROFILE_IMAGE_URL + chatRoomId,
                    LAST_MESSAGE + chatRoomId,
                    LAST_SENT_AT
                    )
            );
        }
        return chatRooms;
    }

}