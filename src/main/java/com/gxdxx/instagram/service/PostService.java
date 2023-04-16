package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.dto.request.PostUpdateRequest;
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

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public PostRegisterResponse registerPost(PostRegisterRequest request, String userNickname) throws IOException {
        User user = userRepository.findByNickname(userNickname).orElseThrow(UserNotFoundException::new);
        String imageUrl = s3Uploader.upload(request.image(), "images");
        Post post = Post.of(request.content(), imageUrl, user);
        return PostRegisterResponse.of(postRepository.save(post));
    }

    public PostUpdateResponse updatePost(PostUpdateRequest request, String userNickname) throws IOException {
        Post updatingPost = postRepository.findById(request.id()).orElseThrow(PostNotFoundException::new);
        if (!updatingPost.getUser().getNickname().equals(userNickname)) {
            throw new UnauthorizedAccessException();
        }
        String imageUrl = s3Uploader.upload(request.image(), "images");
        updatingPost.update(request.content(), imageUrl);
        return PostUpdateResponse.of(updatingPost);
    }

    public SuccessResponse deletePost(Long postId, String requestedUserNickname) {
        Post deletingPost = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (!deletingPost.getUser().getNickname().equals(requestedUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        postRepository.delete(deletingPost);
        return SuccessResponse.of("200 SUCCESS");
    }

}
