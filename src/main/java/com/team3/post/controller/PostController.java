package com.team3.post.controller;


import com.team3.board.service.BoardService;
import com.team3.comment.entity.Comment;
import com.team3.comment.dto.CommentDto;
import com.team3.comment.service.CommentService;
import com.team3.post.dto.PostDto;
import com.team3.post.entity.Post;
import com.team3.post.entity.PostPhoto;
import com.team3.post.service.PostPhotoService;
import com.team3.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final PostPhotoService postPhotoService;

    //boardId에 따른 게시글 목록 조회
    @GetMapping("/boards/{boardId}/posts")
    public String postList(@PathVariable("boardId") Long boardId,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "size", defaultValue = "10") int pageSize,
                           @RequestParam(value = "sort", defaultValue = "updatedAt") String sortBy,
                           @RequestParam(value = "asc", defaultValue = "false") boolean ascending,
                           Model m){

        int page = Math.max(0, pageNumber - 1);
        Page<PostDto> posts = postService.getPostsByBoardId(boardId, page, pageSize, sortBy, ascending);
        String boardTitle = boardService.getBoard(boardId).getTitle();

        m.addAttribute("posts", posts.getContent());
        m.addAttribute("boardId", boardId);
        m.addAttribute("boardTitle", boardTitle);
        m.addAttribute("currentPage", posts.getNumber() + 1);
        m.addAttribute("sortBy", sortBy);
        m.addAttribute("ascending", ascending);
        m.addAttribute("totalPages", posts.getTotalPages());
        m.addAttribute("totalItems", posts.getTotalElements());
        m.addAttribute("pageSize", pageSize);

        return "post/postList";
    }

    //최신 업데이트 순 정렬
    @GetMapping("/boards/{boardId}/posts/updated")
    public String postListOrderByUpdatedAt(@PathVariable("boardId") Long boardId, Model m){
        List<PostDto> posts = postService.getPostsByBoardIdUpdatedAtDesc(boardId);
        String boardTitle = boardService.getBoard(boardId).getTitle();

        m.addAttribute("posts",posts);
        m.addAttribute("boardId", boardId);
        m.addAttribute("boardTitle", boardTitle);

        return "post/postList";
    }

    //조회수 순 정렬
    @GetMapping("/boards/{boardId}/posts/views")
    public String postListOrderByViewsAt(@PathVariable("boardId") Long boardId, Model m){
        List<PostDto> posts = postService.getPostsByBoardIdViewsDesc(boardId);
        String boardTitle = boardService.getBoard(boardId).getTitle();

        m.addAttribute("posts",posts);
        m.addAttribute("boardId", boardId);
        m.addAttribute("boardTitle", boardTitle);

        return "post/postList";
    }

    // 검색 기능
    @GetMapping("/{boardId}/search")
    public String searchPost(@PathVariable("boardId") Long boardId,
                             @RequestParam("keyword") String keyword,
                             Model m){
        List<PostDto> posts = postService.getPostsBySearch(boardId, keyword);
        String boardTitle = boardService.getBoard(boardId).getTitle();

        m.addAttribute("posts",posts);
        m.addAttribute("boardId", boardId);
        m.addAttribute("boardTitle", boardTitle);

        return "post/postList";
    }

    // 게시글 번호에 따른 게시글 조회
    @GetMapping("/posts/{postId}")
    public String postdetail(@PathVariable("postId") Long postId,
                             Model m,
                             HttpSession session){
        //게시글 로직
        Post post = postService.getPostByPostId(postId);
        Long boardId = post.getBoard().getBoardId();
        Long currentSessionUserId = (Long)session.getAttribute("userId");

        Long userId = postService.getPostByPostId(postId).getUser().getId();

        if(!postService.userCheck(currentSessionUserId, userId)) {
            m.addAttribute("user_check", "N");
        }

        postService.incrementViews(postId);

        m.addAttribute("boardId", boardId);
        m.addAttribute("post", post);

        //게시글 사진 로직
        List<PostPhoto> postPhotos = postPhotoService.getPhotosByPostId(Long.valueOf(postId));
        List<String> imageUrls = postPhotos.stream()
                .map(PostPhoto::getImagePath)  // 파일 경로를 가져와서 URL로 변환
                .toList();

        m.addAttribute("imageUrls", imageUrls);


        //댓글 로직
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        m.addAttribute("comments", comments);
        m.addAttribute("currentSessionUserId", currentSessionUserId);
        return "post/post";
    }

    //댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public String createComment(@RequestParam("postId")Long postId,
                                CommentDto commentDto,
                                HttpSession session){

        Long userId = (Long)session.getAttribute("userId");
        commentDto.setUserId(userId);
        commentDto.setPostId(postId);


        commentService.addComment(commentDto);

        return "redirect:/post/postdetail/"+postId;
    }

    //댓글 수정 페이지 제공
    @GetMapping("/comments/{commentId}/edit")
    public String editCommentForm(@RequestParam("commentId") Long commentId,
                                @RequestParam("postId") Long postId,
                                Model m){

        Comment comment = commentService.getCommentById(commentId);

        m.addAttribute("comment", comment);
        m.addAttribute("postId", postId);

        return "post/comment_modify";
    }

    //댓글 수정 처리
    @PutMapping("/comments/{commentId}")
    public String updateComment(@RequestParam("commentId") Long commentId,
                                @RequestParam("content") String content,
                                @RequestParam("postId") Long postId){

        commentService.modifyComment(commentId,content);

        return "redirect:/post/postdetail/" + postId;
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                @RequestParam("postId") Long postId,
                                RedirectAttributes rattr,
                                HttpSession session){

        Long userId = commentService.getCommentById(commentId).getUser().getId();
        Long currentUserId = (Long) session.getAttribute("userId");

        commentService.deleteComment(commentId);

        return "redirect:/post/postdetail/"+postId;
    }

    //게시글 작성
    @PostMapping("/boards/{boardId}/posts")
    public String createPost(@RequestParam("boardId") Long boardId,
                             @RequestParam("images") List<MultipartFile> images,
                             PostDto postDto,
                             HttpSession session){

        //로그인한 사용자의 세션을 postDto에 set
        Long userId = (Long)session.getAttribute("userId");
        postDto.setUserId(userId);
        postDto.setBoardId(boardId);

        try{
            postService.createPost(postDto, images);
        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/create/" + boardId;
        }

        return "redirect:/post/" + boardId;
    }

    //게시판번호에 따른 게시글 작성페이지 이동
    @GetMapping("/create/{boardId}")
    public String createPostForm(@PathVariable("boardId") Long boardId,
                                 Model m,
                                 RedirectAttributes rattr,
                                 HttpSession session){

        if(session.getAttribute("userId")==null){
            rattr.addFlashAttribute("userSession", "N");
            return "redirect:/login";
        }

        m.addAttribute("boardId", boardId);
        return "post/post_create";
    }



    //게시글 번호에 따른 게시글 수정 페이지
    @GetMapping("/posts/{postId}/edit")
    public String modifyForm(@PathVariable("postId") Long postId,
                             Model m,
                             RedirectAttributes rattr,
                             HttpSession session){

        Long userId = postService.getPostByPostId(postId).getUser().getId();

        //글 작성자가 맞으면 수정페이지로 이동, 아니면 메시지 띄운 후 다시 게시글 페이지로
        if(postService.userCheck((Long)session.getAttribute("userId"), userId)){
            Post post = postService.getPostByPostId(postId);

            m.addAttribute("post", post);
            return "post/post_modify";
        };

        rattr.addFlashAttribute("user_check", "N");
        return "redirect:/post/postdetail/" + postId;
    }

    //게시글 번호에 따른 게시글 수정
    @PutMapping("/posts/{postId}")
    public String modifyPost(@PathVariable("postId") Long postId,
                             PostDto postDto){

        postService.modifyPost(postDto);

        return "redirect:/post/postdetail/"+postId;
    }

    //게시글 번호에 따른 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable("postId") Long postId,
                             @RequestParam("boardId") Long boardId,
                             RedirectAttributes rattr,
                             Model m,
                             HttpSession session){

        Long userId = postService.getPostByPostId(postId).getUser().getId();

        //글 작성자가 맞으면 삭제 반영, 아니면 메시지 띄운 후 다시 게시글 페이지로
        if(postService.userCheck((Long)session.getAttribute("userId"), userId)){
            commentService.deleteCommentsByPostId(postId);
            postService.deletePost(postId);

            return "redirect:/post/" + boardId;
        };

        return "redirect:/post/postdetail/"+postId;
    }
}
