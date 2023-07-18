package com.gxdxx.instagram.domain.chatroom.dao;

import com.gxdxx.instagram.domain.chatroom.dto.response.ChatRoomListResponse;
import com.gxdxx.instagram.dto.response.QChatRoomListResponse;
import com.gxdxx.instagram.entity.QChatRoom;
import com.gxdxx.instagram.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ChatRoomListResponse> getChatRoomsByCursor(Long requestingUserId, Long cursor, int limit) {
        QUser userA = new QUser("userA");
        QUser userB = new QUser("userB");
        QChatRoom chatRoom = QChatRoom.chatRoom;

        List<Long> chatIds = jpaQueryFactory
                .select(chatRoom.id)
                .from(chatRoom)
                .where(
                        chatRoom.userA.id.eq(requestingUserId).or(chatRoom.userB.id.eq(requestingUserId))
                )
                .where(chatRoom.id.lt(cursor))
                .orderBy(chatRoom.id.desc())
                .limit(limit)
                .fetch();

        BooleanBuilder userANotRequestingUserId = new BooleanBuilder();
        userANotRequestingUserId.and(chatRoom.userA.id.eq(requestingUserId).not());

        BooleanBuilder userBNotRequestingUserId = new BooleanBuilder();
        userBNotRequestingUserId.and(chatRoom.userB.id.eq(requestingUserId).not());

        return jpaQueryFactory
                .select(new QChatRoomListResponse(chatRoom.id,
                        new CaseBuilder().when(userANotRequestingUserId).then(userA.nickname).otherwise(userB.nickname),
                        new CaseBuilder().when(userANotRequestingUserId).then(userA.profileImageUrl).otherwise(userB.profileImageUrl),
                        chatRoom.lastMessage,
                        chatRoom.lastSentAt))
                .from(chatRoom)
                .join(chatRoom.userA, userA)
                .join(chatRoom.userB, userB)
                .where(chatRoom.id.in(chatIds))
                .orderBy(chatRoom.id.desc())
                .fetch();
    }


}
