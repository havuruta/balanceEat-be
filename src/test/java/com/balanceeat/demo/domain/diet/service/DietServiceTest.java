package com.balanceeat.demo.domain.diet.service;

import com.balanceeat.demo.domain.diet.dto.DietAmountRequestDTO;
import com.balanceeat.demo.domain.diet.dto.DietByDateDTO;
import com.balanceeat.demo.domain.diet.dto.NutritionSummaryDTO;
import com.balanceeat.demo.domain.diet.entity.Diet;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import com.balanceeat.demo.domain.diet.exception.DietNotFoundException;
import com.balanceeat.demo.domain.diet.fixture.DietFixture;
import com.balanceeat.demo.domain.diet.mapper.DietMapper;
import com.balanceeat.demo.domain.diet.service.impl.DietServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DietServiceTest {

    @Mock
    private DietMapper dietMapper;

    @InjectMocks
    private DietServiceImpl dietService;

    private Long testUserId;
    private LocalDate testDate;
    private DietFixture breakfastFixture;
    private DietFixture lunchFixture;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        testDate = LocalDate.now();
        breakfastFixture = DietFixture.breakfast(testUserId, testDate, 300.0, 20.0, 10.0, 40.0);
        lunchFixture = DietFixture.lunch(testUserId, testDate, 500.0, 30.0, 15.0, 60.0);
    }

    @Test
    @DisplayName("식단 추가 시 DietSummary가 생성되어야 함")
    void addDiet_ShouldCreateDietSummary() {
        // given
        Diet breakfast = breakfastFixture.instance();
        DietAmountRequestDTO request = DietAmountRequestDTO.builder()
                .dietId(breakfast.getId())
                .amount(breakfast.getAmount())
                .build();

        given(dietMapper.findDietById(breakfast.getId())).willReturn(breakfast);
        given(dietMapper.findDietsByDate(testUserId, testDate))
                .willReturn(List.of(breakfast));
        given(dietMapper.findDietSummary(testUserId, testDate))
                .willReturn(null);

        // when
        dietService.addDiet(request, testUserId);

        // then
        then(dietMapper).should().insertDiet(any(Diet.class));
        then(dietMapper).should().insertDietSummary(argThat(summary -> {
            assertThat(summary).satisfies(s -> {
                assertThat(s.getTotalCalories()).isCloseTo(300.0, within(0.001));
                assertThat(s.getTotalProtein()).isCloseTo(20.0, within(0.001));
                assertThat(s.getTotalFat()).isCloseTo(10.0, within(0.001));
                assertThat(s.getTotalCarbohydrates()).isCloseTo(40.0, within(0.001));
            });
            return true;
        }));
    }

    @Test
    @DisplayName("식단 수정 시 DietSummary가 업데이트되어야 함")
    void updateDiet_ShouldUpdateDietSummary() {
        // given
        Diet breakfast = breakfastFixture.instance();
        DietAmountRequestDTO request = DietAmountRequestDTO.builder()
                .dietId(breakfast.getId())
                .amount(200.0)
                .build();

        given(dietMapper.findDietById(breakfast.getId())).willReturn(breakfast);
        given(dietMapper.findDietsByDate(testUserId, testDate))
                .willReturn(List.of(breakfast));
        given(dietMapper.findDietSummary(testUserId, testDate))
                .willReturn(DietSummary.builder()
                        .id(1L)
                        .userId(testUserId)
                        .summaryDate(testDate)
                        .breakfastCalories(300.0)
                        .breakfastProtein(20.0)
                        .breakfastFat(10.0)
                        .breakfastCarbohydrates(40.0)
                        .build());

        // when
        dietService.updateDiet(request, testUserId);

        // then
        then(dietMapper).should().updateDiet(any(Diet.class));
        then(dietMapper).should().updateDietSummary(argThat(summary -> {
            assertThat(summary).satisfies(s -> {
                assertThat(s.getTotalCalories()).isCloseTo(300.0, within(0.001));
                assertThat(s.getTotalProtein()).isCloseTo(20.0, within(0.001));
                assertThat(s.getTotalFat()).isCloseTo(10.0, within(0.001));
                assertThat(s.getTotalCarbohydrates()).isCloseTo(40.0, within(0.001));
            });
            return true;
        }));
    }

    @Test
    @DisplayName("식단 삭제 시 DietSummary가 업데이트되어야 함")
    void deleteDiet_ShouldUpdateDietSummary() {
        // given
        Diet breakfast = breakfastFixture.instance();
        ArgumentCaptor<Long> dietIdCaptor = ArgumentCaptor.forClass(Long.class);
        
        given(dietMapper.findDietById(breakfast.getId())).willReturn(breakfast);
        given(dietMapper.findDietsByDate(testUserId, testDate))
                .willReturn(List.of());
        given(dietMapper.findDietSummary(testUserId, testDate))
                .willReturn(DietSummary.builder()
                        .id(1L)
                        .userId(testUserId)
                        .summaryDate(testDate)
                        .breakfastCalories(300.0)
                        .breakfastProtein(20.0)
                        .breakfastFat(10.0)
                        .breakfastCarbohydrates(40.0)
                        .build());

        // when
        dietService.deleteDiet(breakfast.getId());

        // then
        then(dietMapper).should().deleteDiet(dietIdCaptor.capture());
        assertThat(dietIdCaptor.getValue()).isEqualTo(breakfast.getId());
        then(dietMapper).should().updateDietSummary(argThat(summary -> {
            assertThat(summary).satisfies(s -> {
                assertThat(s.getTotalCalories()).isCloseTo(0.0, within(0.001));
                assertThat(s.getTotalProtein()).isCloseTo(0.0, within(0.001));
                assertThat(s.getTotalFat()).isCloseTo(0.0, within(0.001));
                assertThat(s.getTotalCarbohydrates()).isCloseTo(0.0, within(0.001));
            });
            return true;
        }));
    }

    @Test
    @DisplayName("여러 식단 추가 시 DietSummary가 정확하게 계산되어야 함")
    void addMultipleDiets_ShouldCalculateDietSummaryCorrectly() {
        // given
        Diet breakfast = breakfastFixture.instance();
        Diet lunch = lunchFixture.instance();

        DietAmountRequestDTO request1 = DietAmountRequestDTO.builder()
                .dietId(breakfast.getId())
                .amount(breakfast.getAmount())
                .build();
        DietAmountRequestDTO request2 = DietAmountRequestDTO.builder()
                .dietId(lunch.getId())
                .amount(lunch.getAmount())
                .build();

        given(dietMapper.findDietById(breakfast.getId())).willReturn(breakfast);
        given(dietMapper.findDietById(lunch.getId())).willReturn(lunch);
        given(dietMapper.findDietsByDate(testUserId, testDate))
                .willReturn(List.of(breakfast))
                .willReturn(List.of(breakfast, lunch));
        given(dietMapper.findDietSummary(testUserId, testDate))
                .willReturn(null)
                .willReturn(DietSummary.builder()
                        .id(1L)
                        .userId(testUserId)
                        .summaryDate(testDate)
                        .breakfastCalories(300.0)
                        .breakfastProtein(20.0)
                        .breakfastFat(10.0)
                        .breakfastCarbohydrates(40.0)
                        .build());

        // when
        dietService.addDiet(request1, testUserId);
        dietService.addDiet(request2, testUserId);

        // then
        then(dietMapper).should(times(2)).insertDiet(any(Diet.class));
        then(dietMapper).should().insertDietSummary(argThat(summary -> {
            assertThat(summary).satisfies(s -> {
                assertThat(s.getTotalCalories()).isCloseTo(300.0, within(0.001));
                assertThat(s.getTotalProtein()).isCloseTo(20.0, within(0.001));
                assertThat(s.getTotalFat()).isCloseTo(10.0, within(0.001));
                assertThat(s.getTotalCarbohydrates()).isCloseTo(40.0, within(0.001));
            });
            return true;
        }));
        then(dietMapper).should().updateDietSummary(argThat(summary -> {
            assertThat(summary).satisfies(s -> {
                assertThat(s.getTotalCalories()).isCloseTo(800.0, within(0.001));
                assertThat(s.getTotalProtein()).isCloseTo(50.0, within(0.001));
                assertThat(s.getTotalFat()).isCloseTo(25.0, within(0.001));
                assertThat(s.getTotalCarbohydrates()).isCloseTo(100.0, within(0.001));
            });
            return true;
        }));
    }

    @Test
    @DisplayName("존재하지 않는 식단 수정 시 예외가 발생해야 함")
    void updateDiet_ShouldThrowException_WhenDietNotFound() {
        // given
        DietAmountRequestDTO request = DietAmountRequestDTO.builder()
                .dietId(999L)
                .amount(100.0)
                .build();

        given(dietMapper.findDietById(999L)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> dietService.updateDiet(request, testUserId))
                .isInstanceOf(DietNotFoundException.class);
    }
}