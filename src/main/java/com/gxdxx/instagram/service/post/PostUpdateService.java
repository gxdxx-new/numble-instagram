package com.gxdxx.instagram.service.post;

import com.gxdxx.instagram.dto.request.PostUpdateRequest;
import com.gxdxx.instagram.dto.response.PostUpdateResponse;
import com.gxdxx.instagram.entity.Post;
import com.gxdxx.instagram.exception.PostNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.repository.PostRepository;
import com.gxdxx.instagram.config.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PostUpdateService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;

    public PostUpdateResponse updatePost(PostUpdateRequest request, String requestingUserNickname) {
        Post updatingPost = postRepository.findById(request.id())
                .orElseThrow(PostNotFoundException::new);
        if (!updatingPost.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        String imageUrl = s3Uploader.upload(request.image(), "images");
        updatingPost.update(request.content(), imageUrl);
        return PostUpdateResponse.of(updatingPost);
    }

}
