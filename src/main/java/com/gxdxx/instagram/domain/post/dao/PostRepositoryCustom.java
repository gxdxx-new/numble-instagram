package com.gxdxx.instagram.domain.post.dao;

import com.gxdxx.instagram.dto.response.PostFeedResponse;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostFeedResponse> getPostsByCursor(Long requestingUserId, Long cursor, int limit);

}
