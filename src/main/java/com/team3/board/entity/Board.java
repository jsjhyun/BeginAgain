package com.team3.board.entity;

import com.team3.global.common.BaseTimeEntity;
import com.team3.post.entity.Post;
import com.team3.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board")
@Getter //@Setter
@Builder
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Post> posts;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt; // 삭제 시간

    @Column(name = "is_deleted", nullable = true)
    private boolean isDeleted; // 삭제 여부

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void deleteBoard() {
        this.deletedAt = LocalDateTime.now();
        this.isDeleted = true;
    }
}
