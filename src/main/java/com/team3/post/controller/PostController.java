package com.team3.post.controller;


import com.team3.board.service.BoardService;
import com.team3.comment.entity.Comment;
import com.team3.comment.dto.CommentDto;
import com.team3.comment.service.CommentService;
import com.team3.post.dto.PostDto;
import com.team3.post.entity.Post;
import com.team3.post.entity.PostPhotoEntity;
import com.team3.post.service.PostPhotoService;
import com.team3.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostPhotoService postPhotoService;


    //boardId에 따른 게시글 목록 조회
    @GetMapping("/{boardId}")
    public String postList(@PathVariable("boardId") Integer boardId,
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
    @GetMapping("/{boardId}/updatedAt")
    public String postListOrderByUpdatedAt(@PathVariable("boardId") Integer boardId, Model m){
        List<PostDto> posts = postService.getPostsByBoardIdUpdatedAtDesc(boardId);
        String boardTitle = boardService.getBoard(boardId).getTitle();

        m.addAttribute("posts",posts);
        m.addAttribute("boardId", boardId);
        m.addAttribute("boardTitle", boardTitle);

        return "post/postList";
    }

    //조회수 순 정렬
    @GetMapping("/{boardId}/views")
    public String postListOrderByViewsAt(@PathVariable("boardId") Integer boardId, Model m){
        List<PostDto> posts = postService.getPostsByBoardIdViewsDesc(boardId);
        String boardTitle = boardService.getBoard(boardId).getTitle();

        m.addAttribute("posts",posts);
        m.addAttribute("boardId", boardId);
        m.addAttribute("boardTitle", boardTitle);

        return "post/postList";
    }

    // 검색 기능
    @GetMapping("/{boardId}/search")
    public String searchPost(@PathVariable("boardId") Integer boardId,
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
    @GetMapping("/postdetail/{postId}")
    public String postdetail(@PathVariable("postId") Integer postId,
                             Model m,
                             HttpSession session){
        //게시글 로직
        Post post = postService.getPostByPostId(postId);
        Integer boardId = post.getBoard().getBoardId();
        Integer currentSessionUserId = (Integer)session.getAttribute("userId");

        Integer userId = postService.getPostByPostId(postId).getUser().getId();
        if(!postService.userCheck(currentSessionUserId, userId)) {
            m.addAttribute("user_check", "N");
        }

        postService.incrementViews(postId);

        m.addAttribute("boardId", boardId);
        m.addAttribute("post", post);

        //게시글 사진 로직
        List<PostPhotoEntity> postPhotos = postPhotoService.getPhotosByPostId(postId);
        List<String> imageUrls = postPhotos.stream()
                .map(PostPhotoEntity::getImagePath)  // 파일 경로를 가져와서 URL로 변환
                .toList();

        m.addAttribute("imageUrls", imageUrls);


        //댓글 로직
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        m.addAttribute("comments", comments);
        m.addAttribute("currentSessionUserId", currentSessionUserId);
        return "post/post";
    }

    //댓글 생성
    @PostMapping("/comment/create")
    public String createComment(@RequestParam("postId")Integer postId,
                                CommentDto commentDto,
                                HttpSession session){

        Integer userId = (Integer)session.getAttribute("userId");
        commentDto.setUserId(userId);
        commentDto.setPostId(postId);


        commentService.addComment(commentDto);

        return "redirect:/post/postdetail/"+postId;
    }

    //댓글 수정
    @GetMapping("/comment/modify")
    public String updateComment(@RequestParam("commentId") Integer commentId,
                                @RequestParam("postId") Integer postId,
                                Model m){

        Comment comment = commentService.getCommentById(commentId);

        m.addAttribute("comment", comment);
        m.addAttribute("postId", postId);

        return "post/comment_modify";
    }

    //댓글 수정
    @PostMapping("/comment/modify")
    public String updateComment(@RequestParam("commentId") Integer commentId,
                                @RequestParam("content") String content,
                                @RequestParam("postId") Integer postId){

        commentService.modifyComment(commentId,content);

        return "redirect:/post/postdetail/" + postId;
    }

    //댓글 삭제
    @PostMapping("/comment/delete")
    public String deleteComment(@RequestParam("commentId") Integer commentId,
                                @RequestParam("postId") Integer postId,
                                RedirectAttributes rattr,
                                HttpSession session){

        Integer userId = commentService.getCommentById(commentId).getUser().getId();
        Integer currentUserId = (Integer) session.getAttribute("userId");

        commentService.deleteComment(commentId);

        return "redirect:/post/postdetail/"+postId;
    }

    //게시글 작성
    @PostMapping("/create")
    public String createPost(@RequestParam("boardId") Integer boardId,
                             @RequestParam("images") List<MultipartFile> images,
                             PostDto postDto,
                             HttpSession session){

        //로그인한 사용자의 세션을 postDto에 set
        Integer userId = (Integer)session.getAttribute("userId");
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
    public String createPostForm(@PathVariable("boardId") Integer boardId,
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
    @GetMapping("/modify/{postId}")
    public String modifyForm(@PathVariable("postId") Integer postId,
                             Model m,
                             RedirectAttributes rattr,
                             HttpSession session){

        Integer userId = postService.getPostByPostId(postId).getUser().getId();

        //글 작성자가 맞으면 수정페이지로 이동, 아니면 메시지 띄운 후 다시 게시글 페이지로
        if(postService.userCheck((Integer)session.getAttribute("userId"), userId)){
            Post post = postService.getPostByPostId(postId);

            m.addAttribute("post", post);
            return "post/post_modify";
        };

        rattr.addFlashAttribute("user_check", "N");
        return "redirect:/post/postdetail/" + postId;
    }

    //게시글 번호에 따른 게시글 수정
    @PostMapping("/modify/{postId}")
    public String modifyPost(@PathVariable("postId") Integer postId,
                             PostDto postDto){

        postService.modifyPost(postDto);

        return "redirect:/post/postdetail/"+postId;
    }

    //게시글 번호에 따른 게시글 삭제
    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId") Integer postId,
                             @RequestParam("boardId") Integer boardId,
                             RedirectAttributes rattr,
                             Model m,
                             HttpSession session){

        Integer userId = postService.getPostByPostId(postId).getUser().getId();

        //글 작성자가 맞으면 삭제 반영, 아니면 메시지 띄운 후 다시 게시글 페이지로
        if(postService.userCheck((Integer)session.getAttribute("userId"), userId)){
            commentService.deleteCommentsByPostId(postId);
            postService.deletePost(postId);

            return "redirect:/post/" + boardId;
        };

        return "redirect:/post/postdetail/"+postId;
    }
}
