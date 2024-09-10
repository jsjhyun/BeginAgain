package com.team3.board.service;

import com.team3.board.dto.CreateBoardDto;
import com.team3.board.dto.UpdateBoardDTO;
import com.team3.board.entity.Board;
import com.team3.board.repository.BoardRepository;
import com.team3.board.exception.CustomException;
import com.team3.user.entity.User;
import com.team3.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static com.team3.global.error.ErrorCode.USER_NOT_AUTHENTICATED;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;  // UserService 대신 UserRepository 사용

    // 게시판 생성
    public Board addBoard(CreateBoardDto createBoardDto) {
        // UserRepository를 사용하여 User 엔티티를 조회
        User user = userRepository.findById(createBoardDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        // 새로운 Board 생성
        Board newBoard = Board.builder()
                .title(createBoardDto.getTitle())
                .content(createBoardDto.getContent())
                .user(user) // 연관된 User 엔티티 설정
                .isDeleted(false) // 삭제 여부 false로 설정
                .build();

        boardRepository.save(newBoard);
        return newBoard;
    }

    // 반복적인 게시판 조회 코드를 줄이기 위해 공통 메서드 추가
    private Board findExistingBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시판을 찾을 수 없습니다."));

        if (board.isDeleted()) {
            throw new IllegalStateException("게시판이 삭제되었습니다.");
        }

        return board;
    }

    // 게시판 상세 조회
    public Board getBoard(Long boardId) {
        return findExistingBoard(boardId);
    }

    // 게시판 전체 (리스트) 조회
//    public List<Board> getAllBoards() {
//        return boardRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
//    }

    // 게시판 전체 (리스트) 조회 + 페이징 + 정렬 + 검색
    public Page<Board> getBoards(int page, int size, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return boardRepository.searchByKeyword(keyword, pageable);
        } else {
            return boardRepository.findByIsDeletedFalse(pageable);
        }
    }


    // 게시판 수정
    public Board updateBoard(Long boardId, Long currentUserId, UpdateBoardDTO updateBoardDTO) {
        // 공통 메서드를 사용하여 기존 게시판 조회
        Board existingBoard = findExistingBoard(boardId);

        if (!existingBoard.getUser().getId().equals(currentUserId)) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }

        // 업데이트할 필드만 설정
        if (!updateBoardDTO.getTitle().isEmpty() && !updateBoardDTO.getContent().isEmpty()) {
            existingBoard.updateBoard(updateBoardDTO.getTitle(), updateBoardDTO.getContent());
        }
        // 수정된 게시판 저장
        return boardRepository.save(existingBoard);
    }


    // 게시판 삭제
    public Board deleteBoard(Long boardId, Long currentUserId) {

        // 공통 메서드를 사용하여 기존 게시판 조회
        Board existingBoard = findExistingBoard(boardId);

        if (!existingBoard.getUser().getId().equals(currentUserId)) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }

        // 삭제일자 업데이트
        existingBoard.deleteBoard();

        return boardRepository.save(existingBoard);
    }
}
