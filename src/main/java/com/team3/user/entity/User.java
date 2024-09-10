package com.team3.user.entity;

import com.team3.board.entity.Board;
import com.team3.comment.entity.Comment;
import com.team3.global.common.BaseTimeEntity;
import com.team3.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, length=200)
    private String email;

    @Column(name = "password", nullable = false, length=200)
    private String password;

    @Column(name = "username", nullable = false, length=30)
    private String username;

    @Column(name = "nickname", nullable = false, length=30)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType role; // ADMIN, USER

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public User(String email, String password, String username, String nickname, RoleType role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
    }
    // 닉네임 변경 로직
    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }
}