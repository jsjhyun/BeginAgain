package com.team3.post.repository;

import com.team3.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoard_BoardId(Long boardId, Pageable pageable);
    List<Post> findByBoard_BoardId(Long boardId);
    List<Post> findByBoard_BoardIdOrderByUpdatedAtDesc(Long boardId);
    List<Post> findByBoard_BoardIdOrderByViewsDesc(Long boardId);

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword% AND p.board.id = :boardId")
    List<Post> searchByContent(@Param("boardId")Long boardId, @Param("keyword")String keyword);
}
