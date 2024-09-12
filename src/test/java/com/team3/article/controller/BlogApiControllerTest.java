package com.team3.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.article.dto.AddArticleRequest;
import com.team3.article.entity.Article;
import com.team3.article.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired // 직렬. 역직렬화 인터페이스 필요
    protected ObjectMapper objectMapper; // json <-> java

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        blogRepository.deleteAll(); // truncate - 샘플데이터 삭제
    }

    @DisplayName("addArticle: 블로그 글 추가")
    @Test
    public void addArticle() throws Exception {
        // given
        // url 필요
        final String url = "/api/articles";
        // 블로그 글, 내용
        final String title = "title1";
        final String content = "content1";
        // request 보내야함. dto 만들기
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        // dto >> json 변환 후 request (메시지 바디에 담아야 함)
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // when
        // 데이터를 요청하고 응답을 받는다.
        ResultActions result = mockMvc.perform( // mockMvc.perform 이 ResultActions 로 반환 함
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        // response 응답 메시지 바디 내용과 내가 처음에 작성한 글, 내용을 비교. 검증

        // 생성 완료 응답 코드 확인
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);

        // title 맞는지
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        // content 맞는지
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }
}