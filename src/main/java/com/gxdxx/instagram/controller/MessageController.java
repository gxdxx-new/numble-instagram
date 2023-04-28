package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/messages")
@RestController
public class MessageController {

    private final MessageService messageService;

}
