package com.team3.test.controller;

import com.team3.test.entity.Member;
import com.team3.test.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context; // 실제 동작 환경 구성

    @Autowired
    private MemberRepository memberRepository;

    /*
        MockMvc 객체를 설정
        실제 웹 서버를 구동하지 않고도 HTTP 요청 및 응답을 모킹(mock)하여 테스트 가능
     */
    @BeforeEach
    public void mockMvcSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context) // MockMvc 객체를 빌드하기 위한 클래스
                .build();
    }

    @AfterEach
    public void cleanUp(){
        memberRepository.deleteAll();
    }

    @DisplayName(("getAllMembers: 멤버 조회에 성공"))
    @Test
    public void getAllMembers() throws Exception {
        //given
        final String url = "/test"; // 테스트 할 url로 /test 설정
        Member savedMember = memberRepository.save(new Member(1L, "hyun"));

        //when
        // HTTP 요청을 시뮬레이션하고, 테스트 환경에서 컨트롤러를 호출해 ResultActions 객체에 저장
        final ResultActions result = mockMvc.perform(get(url)); // GET 요청

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(savedMember.getId()))
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));
    }
}