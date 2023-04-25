package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.PostFeedRequest;
import com.gxdxx.instagram.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.dto.request.PostUpdateRequest;
import com.gxdxx.instagram.dto.response.PostFeedResponse;
import com.gxdxx.instagram.dto.response.PostRegisterResponse;
import com.gxdxx.instagram.dto.response.PostUpdateResponse;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Post;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.PostNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.PostRepository;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public PostRegisterResponse registerPost(PostRegisterRequest request, String requestingUserNickname) throws IOException {
        User registeringUser = userRepository.findByNickname(requestingUserNickname)
                .orElseThrow(UserNotFoundException::new);
        String imageUrl = s3Uploader.upload(request.image(), "images");
        Post registeringPost = Post.of(request.content(), imageUrl, registeringUser);
        return PostRegisterResponse.of(postRepository.save(registeringPost));
    }

    public PostUpdateResponse updatePost(PostUpdateRequest request, String requestingUserNickname) throws IOException {
        Post updatingPost = postRepository.findById(request.id())
                .orElseThrow(PostNotFoundException::new);
        if (!updatingPost.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        String imageUrl = s3Uploader.upload(request.image(), "images");
        updatingPost.update(request.content(), imageUrl);
        return PostUpdateResponse.of(updatingPost);
    }

    public SuccessResponse deletePost(Long postId, String requestingUserNickname) {
        Post deletingPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        if (!deletingPost.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        postRepository.delete(deletingPost);
        return SuccessResponse.of("200 SUCCESS");
    }

    public Map<String, Object> getFeed(PostFeedRequest request, String requestingUserNickname) {
        User user = userRepository.findByNickname(requestingUserNickname)
                .orElseThrow(UserNotFoundException::new);
        List<PostFeedResponse> feeds = postRepository.getPostsByCursor(user.getId(), request.cursor(), 5);
        Long cursor = !feeds.isEmpty() ? feeds.get(feeds.size() - 1).getPostId() : 0L;
        Map<String, Object> response = new HashMap<>();
        response.put("cursor", cursor);
        response.put("posts", feeds);

        return response;
    }

}
