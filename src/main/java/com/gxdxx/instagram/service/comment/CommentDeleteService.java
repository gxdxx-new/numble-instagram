package com.gxdxx.instagram.service.comment;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Comment;
import com.gxdxx.instagram.exception.CommentNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentDeleteService {

    private final CommentRepository commentRepository;

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
