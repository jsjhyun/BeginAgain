package com.team3.comment.service;

import com.team3.comment.entity.Comment;
import com.team3.comment.dto.CommentDto;
import com.team3.comment.repository.CommentRepository;
import com.team3.post.entity.Post;
import com.team3.post.repository.PostRepository;
import com.team3.user.entity.User;
import com.team3.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 생성자를 통한 의존성 주입
    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 댓글 추가
    public Comment addComment(CommentDto commentDto) { //content와 author(작성자)를 인자로 받아 새로운 Comment 객체를 생성하고, 이를 데이터베이스에 저장합니다.

        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        Comment newComment = Comment.builder()
                        .content(commentDto.getContent())
                        .user(user)
                        .post(post)
                        .build();



        return commentRepository.save(newComment);  // 댓글을 저장하고 저장된 댓글 반환
    }



    // 모든 댓글 조회
    public List<Comment> getAllComments() { //데이터베이스에서 모든 댓글을 조회하여 리스트로 반환.
        return commentRepository.findAll();  // 모든 댓글을 데이터베이스에서 조회하여 반환
    }

    // 게시글 번호에 따른 모든 댓글 조회
    public List<CommentDto> getCommentsByPostId(Integer postId){
        List<Comment> comments = commentRepository.findByPost_PostId(postId);

        List<CommentDto> commentDtos = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDto commentDto = new CommentDto();
            commentDto.setCommentId(comment.getCommentId());
            commentDto.setContent(comment.getContent());
            commentDto.setUserId(comment.getUser().getId());
            commentDto.setNickname(comment.getUser().getNickname());
            commentDto.setCreateAt(comment.getCreatedAt());
            commentDto.setUpdatedAt(comment.getUpdatedAt());
            commentDto.setPostId(comment.getPost().getPostId());
            commentDtos.add(commentDto);
        }

        return commentDtos;
    }

    // 댓글 수정
    public Comment modifyComment(Integer commentId, String newContent) { //특정 ID의 댓글을 수정.
        Comment comment = commentRepository.findById(commentId) //주어진 ID로 댓글을 검색. 댓글이 없으면 NoSuchElementException을 발생.
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));  // ID로 댓글을 조회하고, 존재하지 않으면 예외 발생
        comment.setContent(newContent);  // 댓글 내용 수정
        return commentRepository.save(comment);  // 수정된 댓글을 저장하고 반환
    }

    // 댓글 삭제
    public void deleteComment(Integer commentId) { //특정 ID의 댓글을 삭제합니다.
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));

        commentRepository.delete(comment);
    }


    // ID로 특정 댓글 조회
    public Comment getCommentById(Integer id) { //특정 ID의 댓글을 조회합니다. 댓글이 없으면 예외를 발생시킵니다.
        return commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));  // ID로 댓글을 조회하고, 존재하지 않으면 예외 발생
    }

    public void deleteCommentsByPostId(Integer postId){
        List<Comment> comments = commentRepository.findByPost_PostId(postId);
        if(!comments.isEmpty()){
            commentRepository.deleteAll(comments);
        }
    }

}


