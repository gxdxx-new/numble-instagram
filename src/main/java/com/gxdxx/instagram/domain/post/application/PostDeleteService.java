package com.gxdxx.instagram.domain.post.application;

import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.post.exception.PostNotFoundException;
import com.gxdxx.instagram.global.auth.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.domain.post.dao.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PostDeleteService {

    private final PostRepository postRepository;

    public SuccessResponse deletePost(Long postId, String nickname) {
        Post postToDelete = findPostById(postId);
        checkPostWriterMatches(postToDelete, nickname);
        deletePostData(postToDelete);
        return SuccessResponse.of("200 SUCCESS");
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private void checkPostWriterMatches(Post postToDelete, String requestedUserNickname) {
        String postWriterNickname = postToDelete.getUser().getNickname();
        if (!postWriterNickname.equals(requestedUserNickname)) {
            throw new UnauthorizedAccessException();
        }
    }

    private void deletePostData(Post post) {
        postRepository.delete(post);
    }

}
