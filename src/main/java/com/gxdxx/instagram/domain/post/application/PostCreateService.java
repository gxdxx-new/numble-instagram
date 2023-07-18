package com.gxdxx.instagram.domain.post.application;

import com.gxdxx.instagram.domain.post.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.domain.post.dto.response.PostRegisterResponse;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.global.config.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class PostCreateService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public PostRegisterResponse createPost(PostRegisterRequest request, String nickname) {
        User userToCreatePost = findUserByNickname(nickname);
        String imageUrl = uploadImage(request.image());
        Post postToCreate = createPost(request.content(), imageUrl, userToCreatePost);
        return PostRegisterResponse.of(savePost(postToCreate));
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private String uploadImage(MultipartFile image) {
        return s3Uploader.upload(image, "images");
    }

    private Post createPost(String content, String imageUrl, User user) {
        return Post.of(content, imageUrl, user);
    }

    private Post savePost(Post post) {
        return postRepository.save(post);
    }

}
