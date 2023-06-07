package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.ReplyRegisterRequest;
import com.gxdxx.instagram.dto.request.ReplyUpdateRequest;
import com.gxdxx.instagram.dto.response.ReplyRegisterResponse;
import com.gxdxx.instagram.dto.response.ReplyUpdateResponse;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Comment;
import com.gxdxx.instagram.entity.Reply;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.CommentNotFoundException;
import com.gxdxx.instagram.exception.ReplyNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.CommentRepository;
import com.gxdxx.instagram.repository.ReplyRepository;
import com.gxdxx.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ReplyRegisterResponse registerReply(ReplyRegisterRequest request, String requestingUserNickname) {
        User registeringUser = userRepository.findByNickname(requestingUserNickname)
                .orElseThrow(UserNotFoundException::new);
        Comment registeringComment = commentRepository.findById(request.commentId())
                .orElseThrow(CommentNotFoundException::new);
        Reply registeringReply = Reply.of(request.content(), registeringUser, registeringComment);
        return ReplyRegisterResponse.of(replyRepository.save(registeringReply));
    }

    public ReplyUpdateResponse updateReply(ReplyUpdateRequest request, String requestingUserNickname) {
        Reply updatingReply = replyRepository.findById(request.id())
                .orElseThrow(ReplyNotFoundException::new);
        if (!updatingReply.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        updatingReply.update(request.content());
        return ReplyUpdateResponse.of(updatingReply);
    }

    public SuccessResponse deleteReply(Long replyId, String requestingUserNickname) {
        Reply deletingReply = replyRepository.findById(replyId)
                .orElseThrow(ReplyNotFoundException::new);
        if (!deletingReply.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        replyRepository.delete(deletingReply);
        return SuccessResponse.of("200 SUCCESS");
    }

}
