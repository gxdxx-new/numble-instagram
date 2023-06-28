package com.gxdxx.instagram.service.post;

import com.gxdxx.instagram.dto.request.PostFeedRequest;
import com.gxdxx.instagram.dto.response.FeedResponse;
import com.gxdxx.instagram.dto.response.PostFeedResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.PostRepository;
import com.gxdxx.instagram.repository.UserRepository;
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

    @Transactional(readOnly = true)
    public FeedResponse findPostFeed(PostFeedRequest request, String requestingUserNickname) {
        User user = userRepository.findByNickname(requestingUserNickname)
                .orElseThrow(UserNotFoundException::new);
        Long cursor = (request.cursor() == null)
                ? postRepository.findMaxPostId().map(maxId -> maxId + 1).orElse(0L)
                : request.cursor();
        List<PostFeedResponse> feeds = postRepository.getPostsByCursor(user.getId(), cursor, 5);
        Long nextCursor = !feeds.isEmpty() ? feeds.get(feeds.size() - 1).getPostId() : 0L;

        return FeedResponse.of(nextCursor, feeds);
    }

}
