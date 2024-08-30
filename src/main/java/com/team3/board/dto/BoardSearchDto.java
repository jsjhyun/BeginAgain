package com.team3.board.dto;

import com.team3.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardSearchDto {

    private Integer boardId;       // 게시판 ID
    private String title;          // 게시판 제목
    private String content;        // 게시판 내용
    private String userName;       // 게시판 작성자의 이름
    private LocalDateTime createdAt; // 게시판 생성일자
    private LocalDateTime updatedAt; // 게시판 수정일자

    // Board로부터 BoardSearchDto로 변환하는 생성자
    public BoardSearchDto(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userName = board.getUser().getUsername();  // User 엔티티에서 사용자 이름 가져오기
        //this.createdAt = board.getCreatedAt();
        // this.updatedAt = board.getUpdatedAt();
    }
}
