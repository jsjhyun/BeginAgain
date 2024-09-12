package com.team3.article.service;

import com.team3.article.dto.AddArticleRequest;
import com.team3.article.entity.Article;
import com.team3.article.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

}
