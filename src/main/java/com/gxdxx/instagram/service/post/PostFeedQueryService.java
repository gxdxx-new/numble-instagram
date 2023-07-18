package com.gxdxx.instagram.service.post;

import com.gxdxx.instagram.dto.request.PostFeedRequest;
import com.gxdxx.instagram.dto.response.FeedResponse;
import com.gxdxx.instagram.dto.response.PostFeedResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class PostFeedQueryService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final int LIMIT_SIZE = 5;
    private final Long DEFAULT_CURSOR = 0L;

    @Transactional(readOnly = true)
    public FeedResponse findPostFeed(PostFeedRequest request, String nickname) {
        User user = findUserByNickname(nickname);
        Long cursor = determineCursor(request.cursor());
        List<PostFeedResponse> feeds = getPostsByCursor(user.getId(), cursor, LIMIT_SIZE);
        Long nextCursor = determineNextCursor(feeds);
        return FeedResponse.of(nextCursor, feeds);
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private Long determineCursor(Long requestCursor) {
        if (requestCursor != null) {
            return requestCursor;
        }
        return findMaxPostIdOrElseDefaultCursor();
    }

    private Long findMaxPostIdOrElseDefaultCursor() {
        return postRepository.findMaxPostId()
                .map(maxId -> maxId + 1)
                .orElse(DEFAULT_CURSOR);
    }

    private List<PostFeedResponse> getPostsByCursor(Long userId, Long cursor, int limit) {
        return postRepository.getPostsByCursor(userId, cursor, limit);
    }

    private Long determineNextCursor(List<PostFeedResponse> feeds) {
        return feeds.stream()
                .map(PostFeedResponse::getPostId)
                .reduce((first, second) -> second)
                .orElse(DEFAULT_CURSOR);
    }

}
