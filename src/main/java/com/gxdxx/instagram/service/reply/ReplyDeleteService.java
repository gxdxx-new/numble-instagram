package com.gxdxx.instagram.service.reply;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Reply;
import com.gxdxx.instagram.exception.ReplyNotFoundException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReplyDeleteService {

    private final ReplyRepository replyRepository;

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
