package com.team3.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PostPhotoDto {
    private Long photoId;
    private Long postId;
    private String imagePath;
}



