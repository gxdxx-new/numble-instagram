package com.gxdxx.instagram.domain.comment.application;

import com.gxdxx.instagram.global.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.domain.comment.exception.CommentNotFoundException;
import com.gxdxx.instagram.global.auth.UnauthorizedAccessException;
import com.gxdxx.instagram.domain.comment.dao.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentDeleteService {

    private final CommentRepository commentRepository;

    public SuccessResponse deleteComment(Long commentId, String nickname) {
        Comment commentToDelete = findCommentById(commentId);
        checkCommentWriterMatches(commentToDelete, nickname);
        deleteCommentData(commentToDelete);
        return SuccessResponse.of("200 SUCCESS");
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    private void checkCommentWriterMatches(Comment commentToDelete, String requestedUserNickname) {
        String commentWriterNickname = commentToDelete.getUser().getNickname();
        if (!commentWriterNickname.equals(requestedUserNickname)) {
            throw new UnauthorizedAccessException();
        }
    }

    private void deleteCommentData(Comment comment) {
        commentRepository.delete(comment);
    }

}
