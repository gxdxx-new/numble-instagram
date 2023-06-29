package com.gxdxx.instagram.service.reply;

import com.gxdxx.instagram.dto.request.ReplyUpdateRequest;
import com.gxdxx.instagram.dto.response.ReplyUpdateResponse;
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
public class ReplyUpdateService {

    private final ReplyRepository replyRepository;

    public ReplyUpdateResponse updateReply(ReplyUpdateRequest request, String requestingUserNickname) {
        Reply updatingReply = replyRepository.findById(request.id())
                .orElseThrow(ReplyNotFoundException::new);
        if (!updatingReply.getUser().getNickname().equals(requestingUserNickname)) {
            throw new UnauthorizedAccessException();
        }
        updatingReply.update(request.content());
        return ReplyUpdateResponse.of(updatingReply);
    }

}
