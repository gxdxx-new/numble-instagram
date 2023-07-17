package com.gxdxx.instagram.service.comment;

import com.gxdxx.instagram.dto.request.CommentRegisterRequest;
import com.gxdxx.instagram.dto.response.CommentRegisterResponse;
import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.PostNotFoundException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.CommentRepository;
import com.gxdxx.instagram.repository.PostRepository;
import com.gxdxx.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentCreateService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentRegisterResponse createComment(CommentRegisterRequest request, String nickname) {
        User userToCreateComment = findUserByNickname(nickname);
        Post commentablePost = findPostById(request.postId());
        Comment createdComment = createComment(request, userToCreateComment, commentablePost);
        return CommentRegisterResponse.of(createdComment);
    }

    public User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    public Comment createComment(CommentRegisterRequest request, User user, Post post) {
        Comment commentToCreate = Comment.of(request.content(), user, post);
        return commentRepository.save(commentToCreate);
    }

}
