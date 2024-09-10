package com.team3.post.dto;

import com.team3.global.common.BaseTimeEntity;
import com.team3.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto extends BaseTimeEntity {
    private Long postId;
    private String title;
    private String content;
    private Long views;
    private Long userId;
    private String nickname;
    private Long boardId;

    // Post -> PostDto
    public PostDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.views = post.getViews();
        this.userId = post.getUser().getId(); //
        this.nickname = post.getUser().getNickname();
        this.boardId = post.getBoard().getBoardId();
        this.createdAt = post.getCreatedAt(); // 상속받은 BaseTimeEntity의 필드
        this.updatedAt = post.getUpdatedAt(); // 상속받은 BaseTimeEntity의 필드
    }
}
