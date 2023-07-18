package com.gxdxx.instagram.domain.reply.application;

import com.gxdxx.instagram.dto.request.ReplyRegisterRequest;
import com.gxdxx.instagram.dto.response.ReplyRegisterResponse;
import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.domain.reply.domain.Reply;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.CommentNotFoundException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.comment.dao.CommentRepository;
import com.gxdxx.instagram.domain.reply.dao.ReplyRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReplyCreateService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public ReplyRegisterResponse createReply(ReplyRegisterRequest request, String nickname) {
        User userToCreateReply = findUserByNickname(nickname);
        Comment commentToCreate = findCommentById(request.commentId());
        Reply replyToCreate = createReply(request.content(), userToCreateReply, commentToCreate);
        return ReplyRegisterResponse.of(saveReply(replyToCreate));
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    private Reply createReply(String content, User user, Comment comment) {
        return Reply.of(content, user, comment);
    }

    private Reply saveReply(Reply reply) {
        return replyRepository.save(reply);
    }

}
