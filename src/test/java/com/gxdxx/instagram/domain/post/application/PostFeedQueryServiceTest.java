package com.gxdxx.instagram.domain.post.application;

import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.domain.post.dto.request.PostFeedRequest;
import com.gxdxx.instagram.domain.post.dto.response.FeedResponse;
import com.gxdxx.instagram.domain.post.dto.response.PostFeedResponse;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostFeedQueryServiceTest {

    @InjectMocks
    private PostFeedQueryService postFeedQueryService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    private static final Long USER_ID = 1L;
    private static final String FOLLOWER = "follower";
    private static final Long FOLLOWING_ID = 2L;
    private static final String FOLLOWING = "following";
    private static final String PASSWORD = "password";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String CONTENT = "content";
    private static final String IMAGE_URL = "imageUrl";
    private static final Long MAX_POST_ID = 10L;
    private static final int LIMIT = 5;
    private static final Long DEFAULT_CURSOR = 0L;
    private static final Long EXIST_CURSOR = 11L;
    private static final Long NEXT_CURSOR = 1L;
    private static final int EXIST_FEED_SIZE = 5;
    private static final int EMPTY_FEED_SIZE = 0;

    @Test
    @DisplayName("[피드 조회] - 성공 (cursor가 null && post가 존재 && feed가 존재)")
    void findPostFeed_withNullCursorAndExistsPostsAndFeeds_shouldSucceed() {
        // given
        PostFeedRequest request = createPostFeedRequest(null);
        User follower = createUser(FOLLOWER);

        List<PostFeedResponse> feeds = createFeed();

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(postRepository.findMaxPostId()).thenReturn(Optional.of(MAX_POST_ID));
        when(postRepository.getPostsByCursor(any(), eq(MAX_POST_ID + 1), eq(LIMIT))).thenReturn(feeds);

        // when
        FeedResponse response = postFeedQueryService.findPostFeed(request, follower.getNickname());

        // then
        assertEquals(response.feeds().size(), EXIST_FEED_SIZE);
        assertEquals(response.nextCursor(), NEXT_CURSOR);
    }

    @Test
    @DisplayName("[피드 조회] - 성공 (cursor가 null && post가 존재 && feed가 존재 x)")
    void findPostFeed_withNullCursorAndExistsPostsAndNotExistsFeeds_shouldSucceed() {
        // given
        PostFeedRequest request = createPostFeedRequest(null);
        User follower = createUser(FOLLOWER);

        List<PostFeedResponse> feeds = new ArrayList<>();

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(postRepository.findMaxPostId()).thenReturn(Optional.of(MAX_POST_ID));
        when(postRepository.getPostsByCursor(any(), eq(MAX_POST_ID + 1), eq(LIMIT))).thenReturn(feeds);

        // when
        FeedResponse response = postFeedQueryService.findPostFeed(request, follower.getNickname());

        // then
        assertEquals(response.feeds().size(), EMPTY_FEED_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[피드 조회] - 성공 (cursor가 null && post가 존재 x && feed가 존재 x)")
    void findPostFeed_withNullCursorAndNotExistsPostsAndFeeds_shouldSucceed() {
        // given
        PostFeedRequest request = createPostFeedRequest(null);
        User follower = createUser(FOLLOWER);

        List<PostFeedResponse> feeds = new ArrayList<>();

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(postRepository.findMaxPostId()).thenReturn(Optional.of(DEFAULT_CURSOR));
        when(postRepository.getPostsByCursor(any(), eq(DEFAULT_CURSOR + 1), eq(LIMIT))).thenReturn(feeds);

        // when
        FeedResponse response = postFeedQueryService.findPostFeed(request, follower.getNickname());

        // then
        assertEquals(response.feeds().size(), EMPTY_FEED_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[피드 조회] - 성공 (cursor가 존재 && feed가 존재)")
    void findPostFeed_withCursorAndExistsFeeds_shouldSucceed() {
        // given
        PostFeedRequest request = createPostFeedRequest(EXIST_CURSOR);
        User follower = createUser(FOLLOWER);

        List<PostFeedResponse> feeds = createFeed();

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(postRepository.getPostsByCursor(any(), eq(EXIST_CURSOR), eq(LIMIT))).thenReturn(feeds);

        // when
        FeedResponse response = postFeedQueryService.findPostFeed(request, follower.getNickname());

        // then
        assertEquals(response.feeds().size(), EXIST_FEED_SIZE);
        assertEquals(response.nextCursor(), NEXT_CURSOR);
    }

    @Test
    @DisplayName("[피드 조회] - 성공 (cursor가 존재 && feed가 존재 x)")
    void findPostFeed_withCursorAndNotExistsFeeds_shouldSucceed() {
        // given
        PostFeedRequest request = createPostFeedRequest(EXIST_CURSOR);
        User follower = createUser(FOLLOWER);

        List<PostFeedResponse> feeds = new ArrayList<>();

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(postRepository.getPostsByCursor(any(), eq(EXIST_CURSOR), eq(LIMIT))).thenReturn(feeds);

        // when
        FeedResponse response = postFeedQueryService.findPostFeed(request, follower.getNickname());

        // then
        assertEquals(response.feeds().size(), EMPTY_FEED_SIZE);
        assertEquals(response.nextCursor(), DEFAULT_CURSOR);
    }

    @Test
    @DisplayName("[피드 조회] - 실패 (존재하지 않는 회원)")
    void findPostFeed_withNonExistingUser_UserNotFoundException() {
        // given
        PostFeedRequest request = createPostFeedRequest(null);

        when(userRepository.findByNickname(FOLLOWER)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> postFeedQueryService.findPostFeed(request, FOLLOWER));
    }

    private PostFeedRequest createPostFeedRequest(Long cursor) {
        return new PostFeedRequest(USER_ID, cursor);
    }

    private User createUser(String nickname) {
        return User.of(nickname, PASSWORD, PROFILE_IMAGE_URL);
    }

    private List<PostFeedResponse> createFeed() {
        List<PostFeedResponse> feeds = new ArrayList<>();
        for (int postId = EXIST_FEED_SIZE; postId > 0; postId--) {
            feeds.add(new PostFeedResponse((long) postId, CONTENT, IMAGE_URL, FOLLOWING_ID, FOLLOWING));
        }
        return feeds;
    }

}