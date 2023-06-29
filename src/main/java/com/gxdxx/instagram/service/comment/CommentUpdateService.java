package com.gxdxx.instagram.service.comment;

import com.gxdxx.instagram.dto.request.CommentUpdateRequest;
import com.gxdxx.instagram.dto.response.CommentUpdateResponse;
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
public class CommentUpdateService {

    private final CommentRepository commentRepository;

    public CommentUpdateResponse updateComment(CommentUpdateRequest request, String requestingUserNickname) {
        Comment updatingComment = commentRepository.findById(request.id())
                .orElseThrow(CommentNotFoundException::new);
        if (!updatingComment.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        updatingComment.update(request.content());
        return CommentUpdateResponse.of(updatingComment);
    }

}
