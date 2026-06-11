package org.example.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {

    @Test
    @DisplayName("pass()는 passed=true, reason=null을 반환한다")
    void pass_returnsTrueAndNullReason() {
        ValidationResult result = ValidationResult.pass();

        assertTrue(result.isPassed());
        assertNull(result.getReason());
    }

    @Test
    @DisplayName("fail()은 passed=false, reason에 전달한 메시지를 반환한다")
    void fail_returnsFalseAndReason() {
        ValidationResult result = ValidationResult.fail("오류 메시지");

        assertFalse(result.isPassed());
        assertEquals("오류 메시지", result.getReason());
    }
}
