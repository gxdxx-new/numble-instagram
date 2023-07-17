package com.gxdxx.instagram.service.reply;

import com.gxdxx.instagram.dto.request.ReplyUpdateRequest;
import com.gxdxx.instagram.dto.response.ReplyUpdateResponse;
import com.gxdxx.instagram.domain.reply.domain.Reply;
import com.gxdxx.instagram.exception.ReplyNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReplyUpdateService {

    private final ReplyRepository replyRepository;

    public ReplyUpdateResponse updateReply(ReplyUpdateRequest request, String nickname) {
        Reply replyToUpdate = findReplyById(request.id());
        checkReplyWriterMatches(replyToUpdate, nickname);
        replyToUpdate.update(request.content());
        return ReplyUpdateResponse.of(replyToUpdate);
    }

    public Reply findReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(ReplyNotFoundException::new);
    }

    private void checkReplyWriterMatches(Reply replyToUpdate, String requestedUserNickname) {
        String replyWriterNickname = replyToUpdate.getUser().getNickname();
        if (!replyWriterNickname.equals(requestedUserNickname)) {
            throw new UnauthorizedAccessException();
        }
    }

}
