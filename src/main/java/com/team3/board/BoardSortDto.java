package com.team3.board;

import com.team3.board.BoardEntity;
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

    // BoardEntity로부터 BoardSortDto로 변환하는 생성자
    public BoardSortDto(BoardEntity board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.createdAt = board.getCreatedAt();
    }
}
