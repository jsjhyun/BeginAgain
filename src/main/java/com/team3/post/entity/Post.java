package com.team3.post.entity;

import com.team3.board.entity.Board;
import com.team3.comment.entity.Comment;
import com.team3.global.common.BaseTimeEntity;
import com.team3.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 2000, nullable = false)
    private String content;

    @Column(name = "views", nullable = false)
    private Long views = 0L; // 조회수

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    /*
        orphanRemoval = false : 다른 부분에서 사용할 경우를 대비하여 적용 x
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostPhoto> postPhoto = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 게시글 create를 위한 생성자
    @Builder
    public Post (String title, String content, User user, Board board) {
        this.title = title;
        this.content = content;
        this.user = user; // 작성자 누구인가
        this.board = board; // 어느 게시판에 속하는지
    }

    public void incrementViews(){
        this.views += 1;
    }

    public void updatePost(String title, String content){
        this.title = title;
        this.content = content;
    }

}
