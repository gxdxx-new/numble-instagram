package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.CommentRegisterRequest;
import com.gxdxx.instagram.dto.request.CommentUpdateRequest;
import com.gxdxx.instagram.dto.response.CommentRegisterResponse;
import com.gxdxx.instagram.dto.response.CommentUpdateResponse;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Comment;
import com.gxdxx.instagram.entity.Post;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.CommentNotFoundException;
import com.gxdxx.instagram.exception.PostNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.CommentRepository;
import com.gxdxx.instagram.repository.PostRepository;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentRegisterResponse registerComment(CommentRegisterRequest request, String requestingUserNickname) {
        User registeringUser = userRepository.findByNickname(requestingUserNickname)
                .orElseThrow(UserNotFoundException::new);
        Post postForComment = postRepository.findById(request.postId())
                .orElseThrow(PostNotFoundException::new);
        Comment registeringComment = Comment.of(request.content(), registeringUser, postForComment);
        return CommentRegisterResponse.of(commentRepository.save(registeringComment));
    }

    public CommentUpdateResponse updateComment(CommentUpdateRequest request, String requestingUserNickname) {
        Comment updatingComment = commentRepository.findById(request.id())
                .orElseThrow(CommentNotFoundException::new);
        if (!updatingComment.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        updatingComment.update(request.content());
        return CommentUpdateResponse.of(updatingComment);
    }

    public SuccessResponse deleteComment(Long commentId, String requestingUserNickname) {
        Comment deletingComment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!deletingComment.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        commentRepository.delete(deletingComment);
        return SuccessResponse.of("200 SUCCESS");
    }
    
}