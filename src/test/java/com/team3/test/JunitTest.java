package com.team3.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JunitTest {

    @DisplayName("1+2는 3이다")
    @Test
    public void test1() {
        // given
        int a = 1;
        int b = 2;
        int expected = 3;

        // when
        int result = a + b;

        //then
        Assertions.assertEquals(expected, result); // 예상값, 결과값
    }
}
