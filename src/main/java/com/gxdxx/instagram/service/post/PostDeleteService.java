package com.gxdxx.instagram.service.post;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Post;
import com.gxdxx.instagram.exception.PostNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PostDeleteService {

    private final PostRepository postRepository;

    public SuccessResponse deletePost(Long postId, String requestingUserNickname) {
        Post deletingPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        if (!deletingPost.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        postRepository.delete(deletingPost);
        return SuccessResponse.of("200 SUCCESS");
    }

}
