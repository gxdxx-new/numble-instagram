package com.gxdxx.instagram.domain.post.application;

import com.gxdxx.instagram.dto.request.PostUpdateRequest;
import com.gxdxx.instagram.dto.response.PostUpdateResponse;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.exception.PostNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.config.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class PostUpdateService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;

    public PostUpdateResponse updatePost(PostUpdateRequest request, String nickname) {
        Post postToUpdate = findPostById(request.id());
        checkPostWriterMatches(postToUpdate, nickname);
        String imageUrl = uploadImage(request.image());
        postToUpdate.update(request.content(), imageUrl);
        return PostUpdateResponse.of(postToUpdate);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private void checkPostWriterMatches(Post postToUpdate, String requestedUserNickname) {
        String postWriterNickname = postToUpdate.getUser().getNickname();
        if (!postWriterNickname.equals(requestedUserNickname)) {
            throw new UnauthorizedAccessException();
        }
    }

    private String uploadImage(MultipartFile image) {
        return s3Uploader.upload(image, "images");
    }

}
