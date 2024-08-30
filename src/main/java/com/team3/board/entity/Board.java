package com.team3.board.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.team3.global.entity.BaseTimeEntity;
import com.team3.post.entity.Post;
import com.team3.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
@Data
@Builder
//@EntityListeners(AuditingEntityListener.class)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer boardId;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // User와의 관계 설정

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;  // 일대다 관계를 위해 List로 변경

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = true)
    private boolean isDeleted;

    // 엔티티가 저장되기 전에 호출됨
    @PrePersist
    @PreUpdate
    public void updateIsDeleted() {
        this.isDeleted = this.deletedAt != null;
    }

}
