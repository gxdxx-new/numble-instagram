package com.gxdxx.instagram.service.comment;

import com.gxdxx.instagram.dto.request.CommentUpdateRequest;
import com.gxdxx.instagram.dto.response.CommentUpdateResponse;
import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.exception.CommentNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.domain.comment.dao.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentUpdateService {

    private final CommentRepository commentRepository;

    public CommentUpdateResponse updateComment(CommentUpdateRequest request, String nickname) {
        Comment commentToUpdate = findCommentById(request.id());
        checkCommentWriterMatches(commentToUpdate, nickname);
        commentToUpdate.update(request.content());
        return CommentUpdateResponse.of(commentToUpdate);
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    private void checkCommentWriterMatches(Comment commentToUpdate, String requestedUserNickname) {
        String commentWriterNickname = commentToUpdate.getUser().getNickname();
        if (!commentWriterNickname.equals(requestedUserNickname)) {
            throw new UnauthorizedAccessException();
        }
    }

}
