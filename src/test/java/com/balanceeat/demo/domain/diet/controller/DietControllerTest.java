package com.balanceeat.demo.domain.diet.controller;

import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;
import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.entity.MealType;
import com.balanceeat.demo.domain.diet.service.DietService;
import com.balanceeat.demo.global.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DietController.class)
class DietControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DietService dietService;

    @Test
    @DisplayName("식단 요약 조회")
    @WithMockUser(username = "1")
    void getDietSummaries() throws Exception {
        // given
        LocalDate start = LocalDate.of(2024, 3, 1);
        LocalDate end = LocalDate.of(2024, 3, 7);
        List<DietSummary> summaries = Arrays.asList(
            DietSummary.create(1L, start),
            DietSummary.create(1L, end)
        );
        when(dietService.getDietSummariesByDateRange(eq(1L), eq(start), eq(end)))
            .thenReturn(summaries);

        // when & then
        mockMvc.perform(get("/api/diets/summary")
                .param("start", start.toString())
                .param("end", end.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("식단 추가")
    @WithMockUser(username = "1")
    void addDiets() throws Exception {
        // given
        DietAddRequestDTO request = new DietAddRequestDTO();
        DietAddRequestDTO.DietSimpleDTO diet = new DietAddRequestDTO.DietSimpleDTO();
        diet.setNutritionId(1L);
        diet.setAmount("100");
        diet.setNote("테스트");
        diet.setMealType(MealType.BREAKFAST);
        diet.setDietDate(LocalDate.now().toString());
        diet.setMealTime(LocalTime.now());
        request.setDiets(Arrays.asList(diet));

        // when & then
        mockMvc.perform(post("/api/diets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(dietService).batchAddDiets(any(), eq(1L));
    }

    @Test
    @DisplayName("특정 날짜 식단 조회")
    @WithMockUser(username = "1")
    void getDietsByDate() throws Exception {
        // given
        LocalDate date = LocalDate.now();
        List<Diet> diets = Arrays.asList(
            Diet.create(1L, 1L, 100, "테스트", MealType.BREAKFAST, date, LocalTime.now())
        );
        when(dietService.getDietsByDate(eq(1L), eq(date))).thenReturn(diets);

        // when & then
        mockMvc.perform(get("/api/diets/date/{date}", date))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("식단 수정")
    @WithMockUser(username = "1")
    void updateDiet() throws Exception {
        // given
        Long dietId = 1L;
        DietUpdateRequestDTO request = new DietUpdateRequestDTO();
        request.setAmount(200);
        request.setNote("수정된 테스트");
        request.setMealType(MealType.LUNCH);
        request.setMealTime(LocalTime.now().toString());

        // when & then
        mockMvc.perform(put("/api/diets/{dietId}", dietId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(dietService).updateDiet(eq(dietId), any(), eq(1L));
    }

    @Test
    @DisplayName("식단 삭제")
    @WithMockUser(username = "1")
    void deleteDiet() throws Exception {
        // given
        Long dietId = 1L;

        // when & then
        mockMvc.perform(delete("/api/diets/{dietId}", dietId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(dietService).deleteDiet(eq(dietId));
    }

    @Test
    @DisplayName("식단 수정용 조회")
    @WithMockUser(username = "1")
    void getDietsForEdit() throws Exception {
        // given
        LocalDate date = LocalDate.now();
        List<Diet> diets = Arrays.asList(
            Diet.create(1L, 1L, 100, "테스트", MealType.BREAKFAST, date, LocalTime.now())
        );
        when(dietService.getDietsByDate(eq(1L), eq(date))).thenReturn(diets);

        // when & then
        mockMvc.perform(get("/api/diets/edit")
                .param("date", date.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray());
    }
} 