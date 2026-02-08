package com.kanga.kickrushapi.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ErrorResponseFormatTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("에러 응답은 code/message/timestamp 포맷을 따른다")
    void shouldReturnStandardErrorFormat() throws Exception {
        mockMvc.perform(get("/api/shoes/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SHOE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
}
