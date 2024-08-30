package com.team3.board.dto;

import com.team3.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardSortDto {

    private Integer boardId;       // 게시판 ID
    private String title;          // 게시판 제목
    private LocalDateTime createdAt; // 게시판 생성일자

    // Board로부터 BoardSortDto로 변환하는 생성자
    public BoardSortDto(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        //this.createdAt = board.getCreatedAt();
    }
}
