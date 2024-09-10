package com.team3.post.repository;

import com.team3.post.entity.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostPhotoRepository extends JpaRepository<PostPhoto,Long> {
    /*
        여러 사진 조회를 위함
        단일 ID 기준으로 여러 엔티티를 반환하는 기능은 없기 때문에 커스텀 메서드 사용
     */
    List<PostPhoto> findByPost_PostId(Long postId);
}
