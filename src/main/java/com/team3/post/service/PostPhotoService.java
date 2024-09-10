package com.team3.post.service;

import com.team3.post.entity.PostPhoto;
import com.team3.post.repository.PostPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostPhotoService {
    private final PostPhotoRepository postPhotoRepository;

    public List<PostPhoto> getPhotosByPostId(Long postId){
        return postPhotoRepository.findByPost_PostId(postId);
    }
}
