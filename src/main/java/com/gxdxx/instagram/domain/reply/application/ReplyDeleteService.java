package com.gxdxx.instagram.domain.reply.application;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.reply.domain.Reply;
import com.gxdxx.instagram.exception.ReplyNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.domain.reply.dao.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReplyDeleteService {

    private final ReplyRepository replyRepository;

    public SuccessResponse deleteReply(Long replyId, String nickname) {
        Reply replyToDelete = findReplyById(replyId);
        checkReplyWriterMatches(replyToDelete, nickname);
        deleteReplyData(replyToDelete);
        return SuccessResponse.of("200 SUCCESS");
    }

    public Reply findReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(ReplyNotFoundException::new);
    }

    private void checkReplyWriterMatches(Reply replyToDelete, String requestedUserNickname) {
        String replyWriterNickname = replyToDelete.getUser().getNickname();
        if (!replyWriterNickname.equals(requestedUserNickname)) {
            throw new UnauthorizedAccessException();
        }
    }

    private void deleteReplyData(Reply reply) {
        replyRepository.delete(reply);
    }

}
