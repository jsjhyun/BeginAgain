package com.team3.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentDto {
    private Integer commentId;
    private String content;
    private Timestamp createAt;
    private Timestamp updatedAt;
    private Integer userId;
    private String nickname;
    private Integer postId;
}
