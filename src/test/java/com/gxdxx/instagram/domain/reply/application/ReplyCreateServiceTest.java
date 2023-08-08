package com.gxdxx.instagram.domain.reply.application;

import com.gxdxx.instagram.domain.comment.dao.CommentRepository;
import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.domain.comment.exception.CommentNotFoundException;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.reply.dao.ReplyRepository;
import com.gxdxx.instagram.domain.reply.domain.Reply;
import com.gxdxx.instagram.domain.reply.dto.request.ReplyRegisterRequest;
import com.gxdxx.instagram.domain.reply.dto.response.ReplyRegisterResponse;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplyCreateServiceTest {

    @InjectMocks
    private ReplyCreateService replyCreateService;
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;

    private static final String NICKNAME = "nickname";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String POST_CONTENT = "postContent";
    private static final String POST_IMAGE_URL = "postImageUrl";
    private static final Long COMMENT_ID = 1L;
    private static final String COMMENT_CONTENT = "commentContent";
    private static final String REPLY_CONTENT = "replyContent";

    @Test
    @DisplayName("[답글 등록] - 성공")
    void createReply_withValidRequest_shouldSucceed() {
        // given
        ReplyRegisterRequest request = createReplyRegisterRequest();
        User user = createUser();
        Post post = createPost(user);
        Comment comment = createComment(user, post);
        Reply reply = createReply(user, comment);

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(commentRepository.findById(request.commentId())).thenReturn(Optional.of(comment));
        when(replyRepository.save(any(Reply.class))).thenReturn(reply);

        // when
        ReplyRegisterResponse response = replyCreateService.createReply(request, NICKNAME);

        // then
        assertEquals(request.content(), response.content());
    }

    @Test
    @DisplayName("[답글 등록] - 실패 (존재하지 않는 유저)")
    void createReply_withNonExistingUser_shouldThrowUserNotFoundException() {
        // given
        ReplyRegisterRequest request = createReplyRegisterRequest();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(UserNotFoundException.class, () -> replyCreateService.createReply(request, NICKNAME));
    }

    @Test
    @DisplayName("[답글 등록] - 실패 (존재하지 않는 댓글)")
    void createReply_withNonExistingComment_shouldThrowCommentNotFoundException() {
        // given
        ReplyRegisterRequest request = createReplyRegisterRequest();
        User user = createUser();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(commentRepository.findById(request.commentId())).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(CommentNotFoundException.class, () -> replyCreateService.createReply(request, NICKNAME));
    }

    private User createUser() {
        return User.of(NICKNAME, ENCODED_PASSWORD, PROFILE_IMAGE_URL);
    }

    private Post createPost(User user) {
        return Post.of(POST_CONTENT, POST_IMAGE_URL, user);
    }

    private Comment createComment(User user, Post post) {
        return Comment.of(COMMENT_CONTENT, user, post);
    }

    private Reply createReply(User user, Comment comment) {
        return Reply.of(REPLY_CONTENT, user, comment);
    }

    private ReplyRegisterRequest createReplyRegisterRequest() {
        return new ReplyRegisterRequest(COMMENT_ID, REPLY_CONTENT);
    }

}