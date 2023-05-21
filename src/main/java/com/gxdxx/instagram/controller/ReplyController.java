package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.dto.request.ReplyRegisterRequest;
import com.gxdxx.instagram.dto.request.ReplyUpdateRequest;
import com.gxdxx.instagram.dto.response.ReplyRegisterResponse;
import com.gxdxx.instagram.dto.response.ReplyUpdateResponse;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ReplyRegisterResponse registerReply(
            @RequestBody @Valid ReplyRegisterRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return replyService.registerReply(request, principal.getName());
    }

    @PutMapping("/{id}")
    public ReplyUpdateResponse updateReply(
            @RequestBody @Valid ReplyUpdateRequest request,
            BindingResult bindingResult,
            Principal principal
    ) {
        validateRequest(bindingResult);
        return replyService.updateReply(request, principal.getName());
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteReply(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        return replyService.deleteReply(id, principal.getName());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

}
