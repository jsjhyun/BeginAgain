package com.team3.board.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UpdateBoardDTO {
    private String title;
    private String content;
}
