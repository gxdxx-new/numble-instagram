package com.gxdxx.instagram.service;

import com.gxdxx.instagram.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

}
