package com.balanceeat.demo.domain.diet.service;

import com.balanceeat.demo.domain.diet.dto.DietAddRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietUpdateRequestDTO;
import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.entity.MealType;
import com.balanceeat.demo.domain.diet.exception.DietNotFoundException;
import com.balanceeat.demo.domain.diet.mapper.DietMapper;
import com.balanceeat.demo.domain.diet.mapper.DietSummaryMapper;
import com.balanceeat.demo.domain.diet.service.impl.DietServiceImpl;
import com.balanceeat.demo.domain.nutrition.entity.Nutrition;
import com.balanceeat.demo.domain.nutrition.mapper.NutritionMapper;
import com.balanceeat.demo.global.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DietServiceTest {

    @Mock
    private DietMapper dietMapper;

    @Mock
    private DietSummaryMapper dietSummaryMapper;

    @Mock
    private NutritionMapper nutritionMapper;

    @InjectMocks
    private DietServiceImpl dietService;

    private Long testUserId;
    private LocalDate testDate;
    private Nutrition testNutrition;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        testDate = LocalDate.now();
        testNutrition = Nutrition.builder()
            .id(1L)
            .name("테스트 음식")
            .calories(300.0)
            .protein(20.0)
            .fat(10.0)
            .carbohydrates(40.0)
            .build();
    }

    @Test
    @DisplayName("식단 일괄 추가")
    void batchAddDiets() {
        // given
        DietAddRequestDTO request = new DietAddRequestDTO();
        DietAddRequestDTO.DietSimpleDTO diet = new DietAddRequestDTO.DietSimpleDTO();
        diet.setNutritionId(1L);
        diet.setAmount("100");
        diet.setNote("테스트");
        diet.setMealType(MealType.BREAKFAST);
        diet.setDietDate(testDate.toString());
        diet.setMealTime(LocalTime.now());
        request.setDiets(Arrays.asList(diet));

        given(nutritionMapper.findByIds(any())).willReturn(Arrays.asList(testNutrition));
        given(dietSummaryMapper.findByDateAndUserId(eq(testDate), eq(testUserId))).willReturn(null);

        // when
        dietService.batchAddDiets(request, testUserId);

        // then
        verify(dietMapper).batchInsert(any());
        verify(dietSummaryMapper).insert(any());
    }

    @Test
    @DisplayName("식단 요약 조회")
    void getDietSummariesByDateRange() {
        // given
        LocalDate start = testDate;
        LocalDate end = testDate.plusDays(7);
        List<DietSummary> expectedSummaries = Arrays.asList(
            DietSummary.create(testUserId, start),
            DietSummary.create(testUserId, end)
        );
        given(dietSummaryMapper.findByDateRange(eq(testUserId), eq(start), eq(end)))
            .willReturn(expectedSummaries);

        // when
        List<DietSummary> actualSummaries = dietService.getDietSummariesByDateRange(testUserId, start, end);

        // then
        assertThat(actualSummaries).hasSize(2);
        assertThat(actualSummaries).isEqualTo(expectedSummaries);
    }

    @Test
    @DisplayName("식단 수정")
    void updateDiet() {
        // given
        Long dietId = 1L;
        Diet diet = Diet.create(testUserId, 1L, 100, "테스트", MealType.BREAKFAST, testDate, LocalTime.now());
        DietUpdateRequestDTO request = new DietUpdateRequestDTO();
        request.setAmount(200);
        request.setNote("수정된 테스트");
        request.setMealType(MealType.LUNCH);
        request.setMealTime(LocalTime.now().toString());

        given(dietMapper.findById(dietId)).willReturn(Optional.of(diet));

        // when
        dietService.updateDiet(dietId, request, testUserId);

        // then
        verify(dietMapper).update(any(Diet.class));
    }

    @Test
    @DisplayName("존재하지 않는 식단 수정 시 예외 발생")
    void updateDiet_ShouldThrowException_WhenDietNotFound() {
        // given
        Long dietId = 999L;
        DietUpdateRequestDTO request = new DietUpdateRequestDTO();
        given(dietMapper.findById(dietId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> dietService.updateDiet(dietId, request, testUserId))
            .isInstanceOf(DietNotFoundException.class);
    }

    @Test
    @DisplayName("다른 사용자의 식단 수정 시 예외 발생")
    void updateDiet_ShouldThrowException_WhenNotOwner() {
        // given
        Long dietId = 1L;
        Long otherUserId = 2L;
        Diet diet = Diet.create(otherUserId, 1L, 100, "테스트", MealType.BREAKFAST, testDate, LocalTime.now());
        DietUpdateRequestDTO request = new DietUpdateRequestDTO();

        given(dietMapper.findById(dietId)).willReturn(Optional.of(diet));

        // when & then
        assertThatThrownBy(() -> dietService.updateDiet(dietId, request, testUserId))
            .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("식단 삭제")
    void deleteDiet() {
        // given
        Long dietId = 1L;

        // when
        dietService.deleteDiet(dietId);

        // then
        verify(dietMapper).delete(eq(dietId));
    }

    @Test
    @DisplayName("특정 날짜 식단 조회")
    void getDietsByDate() {
        // given
        List<Diet> expectedDiets = Arrays.asList(
            Diet.create(testUserId, 1L, 100, "테스트", MealType.BREAKFAST, testDate, LocalTime.now())
        );
        given(dietMapper.findByDate(eq(testUserId), eq(testDate))).willReturn(expectedDiets);

        // when
        List<Diet> actualDiets = dietService.getDietsByDate(testUserId, testDate);

        // then
        assertThat(actualDiets).hasSize(1);
        assertThat(actualDiets).isEqualTo(expectedDiets);
    }
}