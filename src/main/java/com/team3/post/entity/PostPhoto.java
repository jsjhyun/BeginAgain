package com.team3.post.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
// @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_photo")
public class PostPhoto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_photo_id")
    private Long photoId;

    // 지연로딩 : 필요한 시점에만 Post 데이터를 가져오기 위해 -> 성능 최적
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "image_path")
    private String imagePath;

    public PostPhoto(Post post, String imagePath){
        this.post = post;
        this.imagePath = imagePath;
    }
}


