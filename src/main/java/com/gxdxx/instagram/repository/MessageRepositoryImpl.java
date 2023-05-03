package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.dto.response.MessageListResponse;
import com.gxdxx.instagram.dto.response.QChatRoomListResponse;
import com.gxdxx.instagram.dto.response.QMessageListResponse;
import com.gxdxx.instagram.entity.QChatRoom;
import com.gxdxx.instagram.entity.QMessage;
import com.gxdxx.instagram.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<MessageListResponse> getMessagesByCursor(Long requestingUserId, Long chatRoomId, Long cursor, int limit) {
        QUser sender = new QUser("sender");
        QMessage message = QMessage.message;

        List<Long> messageIds = jpaQueryFactory
                .select(message.id)
                .from(message)
                .where(message.chatRoom.id.eq(chatRoomId))
                .where(message.id.lt(cursor))
                .orderBy(message.id.desc())
                .limit(limit)
                .fetch();

        return jpaQueryFactory
                .select(new QMessageListResponse(message.id, sender.nickname, sender.profileImageUrl, message.content, message.sentAt))
                .from(message)
                .join(message.sender, sender)
                .where(message.id.in(messageIds))
                .orderBy(message.id.desc())
                .fetch();
    }

}
