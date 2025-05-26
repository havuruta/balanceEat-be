package com.balanceeat.demo.domain.ai.service;

import java.time.Duration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.balanceeat.demo.domain.ai.entity.DietScoreResult;
import com.balanceeat.demo.domain.diet.dto.ai.UserDietDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class OneTimeChatService {
    
    private final ChatClient chatClient;   // Spring AI가 자동 주입 :contentReference[oaicite:5]{index=5}
    private final ObjectMapper mapper;
    
    /** Mono<DietScoreResult> 로 반환 → 호출부에서 zip/flatMap 으로 병렬 */
    public Mono<DietScoreResult> ask(UserDietDTO dto) {
        return chatClient.prompt()
            .user(buildPrompt(dto))
            .stream()               // Flux<String>
            .content()
            .collectList()          // GPT가 chunk 로 보낼 수 있음
            .map(list -> String.join("", list))
            .map(this::parse)
            .timeout(Duration.ofSeconds(30))
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));
    }
    
    
    private DietScoreResult parse(String json) {
        try { return mapper.readValue(json, DietScoreResult.class); }
        catch (Exception e) { throw new IllegalArgumentException("GPT format error", e); }
    }

    private String buildPrompt(UserDietDTO dto) {
        return """
        [프로필]
        성별:%s|키:%dcm|체중:%dkg|질환:%s|식습관:%s|선호:%s
        [섭취]
        아침 섭취 칼로리:%.2f kcal|점심 섭취 칼로리:%.2f kcal|저녁 섭취 칼로리:%.2f kcal|간식 섭취 칼로리:%.2f kcal|야식 섭취 칼로리:%.2f kcal
        총 칼로리 :%.2f kcal| 총 탄수화물:%.2f g| 총 단백질:%.2f g| 총 지방:%.2f g
        ---
        출력 규칙
        1) 첫 줄: 0~100 사이 정수(점수) **숫자만** 적습니다.
        2) 둘째 줄: 한국어로 15~40자, 한 문장으로 식단 피드백을 작성합니다.
        3) 위 두 줄 외의 다른 문구·기호·줄바꿈은 절대 포함하지 마세요.
        """.formatted(
            dto.getUserResponseDTO().getGender(),
            dto.getUserResponseDTO().getHeight(),
            dto.getUserResponseDTO().getWeight(),
            dto.getUserResponseDTO().getDiseaseCode(),
            dto.getUserResponseDTO().getDietHabit(),
            dto.getUserResponseDTO().getFoodPreference(),
            dto.getDietSummaryDTO().getBreakfastCalories(),
            dto.getDietSummaryDTO().getLunchCalories(),
            dto.getDietSummaryDTO().getDinnerCalories(),
            dto.getDietSummaryDTO().getSnackCalories(),
            dto.getDietSummaryDTO().getNightCalories(),
            dto.getDietSummaryDTO().getTotalCalories(),
            dto.getDietSummaryDTO().getTotalCarbohydrates(),
            dto.getDietSummaryDTO().getTotalProtein(),
            dto.getDietSummaryDTO().getTotalFat()
        );
    }
}
