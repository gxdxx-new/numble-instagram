package com.gxdxx.instagram.domain.message.dao;

import com.gxdxx.instagram.domain.message.dto.response.MessageListResponse;
import com.gxdxx.instagram.domain.message.dto.response.QMessageListResponse;
import com.gxdxx.instagram.domain.message.domain.QMessage;
import com.gxdxx.instagram.domain.user.domain.QUser;
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
