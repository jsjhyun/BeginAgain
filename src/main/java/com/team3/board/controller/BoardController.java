package com.team3.board.controller;

import com.team3.board.*;
import com.team3.board.dto.CreateBoardDto;
import com.team3.board.dto.UpdateBoardDTO;
import com.team3.board.entity.Board;
import com.team3.board.service.BoardService;
import com.team3.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PostService postService; // PostService 추가
    private final BoardUtils boardUtils;

    /*
    GET -> 목록 : 모든 게시판 조회 후 목록 페이지로 이동
    @GetMapping("")
    public String getAllBoards(Model model) {
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "board/listBoards";  // listBoards.html로 이동
    }
    */

    @GetMapping
    public String getAllBoards(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "9") int size,
                               @RequestParam(value = "sortField", defaultValue = "createdAt") String sortField,
                               @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                               @RequestParam(value = "keyword", required = false) String keyword) {
        Page<Board> boards = boardService.getBoards(page, size, sortField, sortDir, keyword);
        model.addAttribute("boards", boards.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boards.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword); // 검색 키워드를 모델에 추가
        return "board/listBoards";
    }

    // GET {id} -> 조회 : ID로 게시판 조회 후 상세 페이지로 이동
    @GetMapping("/{id}")
    public String getBoard(@PathVariable("id") Long id, Model model) {
        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/viewBoard";  // viewBoard.html로 이동
    }

    // GET create -> 작성 : Create form 페이지로 이동
    @GetMapping("/create")
    public String showCreateBoardForm(HttpSession session, Model model) {

        Long userId = boardUtils.checkUserSession(session);
        // 만약 userId가 null이면 로그인 페이지로 리다이렉트
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("userId", userId);
        return "board/createBoard"; // createBoard.html로 이동
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEntity(@RequestBody CreateBoardDto createBoardDto, HttpSession session) {

        Long sessionUserId = boardUtils.checkUserSession(session);
        // userId가 null인지 체크하고, null일 경우 로그인
        if (sessionUserId == null) {
            return boardUtils.buildRedirectLogin(sessionUserId);
        }

        createBoardDto.setUserId(sessionUserId);
        boardService.addBoard(createBoardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("redirect:/board");
    }

    // GET edit/{id} -> 수정 : 게시판 수정 폼으로 이동
    @GetMapping("/edit/{id}")
    public String showEditBoardForm(@PathVariable("id") Long id, Model model, HttpSession session) {

        Long sessionUserId = boardUtils.checkUserSession(session);
        // userId가 null인지 체크하고, null일 경우 로그인
        if (sessionUserId == null) {
            return "redirect:/login";
        }

        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/editBoard";
    }

    @GetMapping("/edit/validate/{id}")
    public ResponseEntity<String> validateEditBoardForm(@PathVariable("id") Long id, HttpSession session) {

        Long sessionUserId = boardUtils.checkUserSession(session);
        // userId가 null인지 체크하고, null일 경우 로그인
        if (sessionUserId == null) {
            return boardUtils.buildRedirectLogin(sessionUserId);
        }

        Board board = boardService.getBoard(id);
        Long authorUserId = board.getUser().getId();

        if (!authorUserId.equals(sessionUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저 정보가 다릅니다.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("수정페이지로 접근해도 됩니다.");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateBoard(@PathVariable("id") Long id,
                                              @RequestBody UpdateBoardDTO updateBoardDTO,
                                              HttpSession session) {

        Long sessionUserId = boardUtils.checkUserSession(session);
        // userId가 null인지 체크하고, null일 경우 로그인
        if (sessionUserId == null) {
            return boardUtils.buildRedirectLogin(sessionUserId);
        }

        boardService.updateBoard(id, sessionUserId, updateBoardDTO);
        return ResponseEntity.status(HttpStatus.OK).body("redirect:/board/" + id);
    }

    // DELETE delete/{id} -> 삭제(DB저장)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBoardRest(@PathVariable("id") Long id, HttpSession session) {

        Long sessionUserId = boardUtils.checkUserSession(session);
        // userId가 null인지 체크하고, null일 경우 로그인
        if (sessionUserId == null) {
            return boardUtils.buildRedirectLogin(sessionUserId);
        }

//        Board board = boardService.getBoard(id);
//        Long authorUserId = board.getUser().getId();

        // 먼저 게시판에 속한 게시글들을 삭제
        postService.deletePostsByBoardId(id); // 게시글 삭제 추가

        // 이후 게시판 자체를 삭제
        boardService.deleteBoard(id, sessionUserId);

        return ResponseEntity.status(HttpStatus.OK).body("Board deleted successfully.");
    }
}
