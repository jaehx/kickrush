package com.kanga.kickrushapi.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReleaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("발매 목록 조회는 페이지 형식으로 응답한다")
    void shouldReturnReleasesPage() throws Exception {
        mockMvc.perform(get("/api/releases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20));
    }

    @Test
    @DisplayName("상태로 발매 목록을 필터링할 수 있다")
    void shouldFilterReleasesByStatus() throws Exception {
        mockMvc.perform(get("/api/releases").param("status", "ONGOING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("ONGOING"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("발매 상세 조회는 사이즈별 재고를 포함한다")
    void shouldReturnReleaseDetailWithSizes() throws Exception {
        mockMvc.perform(get("/api/releases/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.shoe.name").isNotEmpty())
                .andExpect(jsonPath("$.sizes").isArray())
                .andExpect(jsonPath("$.sizes[0].size").value(250))
                .andExpect(jsonPath("$.sizes[0].stock").value(10));
    }

    @Test
    @DisplayName("존재하지 않는 발매는 404를 반환한다")
    void shouldReturnNotFoundWhenReleaseMissing() throws Exception {
        mockMvc.perform(get("/api/releases/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RELEASE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("발매 정보를 찾을 수 없습니다."))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
}
