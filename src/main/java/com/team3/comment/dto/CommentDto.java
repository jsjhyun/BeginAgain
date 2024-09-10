package com.team3.comment.dto;

import com.team3.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto extends BaseTimeEntity {
    private Long commentId;
    private String content;
    private Long userId;
    private String nickname;
    private Long postId;
}
