package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.dto.response.PostFeedResponse;
import com.gxdxx.instagram.dto.response.QPostFeedResponse;
import com.gxdxx.instagram.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<PostFeedResponse> getPostsByCursor(Long requestingUserId, Long cursor, int limit) {
        QPost post = QPost.post;
        QUser user = QUser.user;
        QFollow follow = QFollow.follow;

        List<Long> followings = jpaQueryFactory
                .select(follow.following.id)
                .from(follow)
                .where(follow.follower.id.eq(requestingUserId))
                .fetch();

        List<PostFeedResponse> postsQuery = jpaQueryFactory
                .select(new QPostFeedResponse(post.id, post.content, post.imageUrl, user.id, user.nickname))
                .from(post)
                .join(post.user, user)
                .on(post.user.id.in(followings))
                .where(post.id.lt(cursor))
                .orderBy(post.id.desc())
                .limit(limit)
                .fetch();

        return postsQuery;
    }

}
