package com.gxdxx.instagram.controller;

import com.gxdxx.instagram.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/follows")
@RestController
public class FollowController {

    private final FollowService followService;

}
