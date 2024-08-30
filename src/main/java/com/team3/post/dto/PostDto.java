package com.team3.post.dto;

import com.team3.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Integer postId;
    private String title;
    private String content;
    private Integer views;
    private Integer userId;
    private String nickname;
    private Integer boardId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public PostDto(Post Post) {
        this.postId = Post.getPostId();
        this.title = Post.getTitle();
        this.content = Post.getContent();
        this.views = Post.getViews();
        this.userId = Post.getUser().getId();
        this.nickname = Post.getUser().getNickname();
        this.boardId = Post.getBoard().getBoardId();
        this.createdAt = Post.getCreatedAt();
        this.updatedAt = Post.getUpdatedAt();
    }
}




