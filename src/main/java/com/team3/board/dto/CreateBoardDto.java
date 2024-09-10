package com.team3.board.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CreateBoardDto {
    private String title;
    private String content;
    private Long userId;  // User의 ID를 받도록 설정
}
