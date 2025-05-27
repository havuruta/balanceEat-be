package com.balanceeat.demo.ai.service;

import static org.springframework.ai.chat.memory.ChatMemory.*;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.balanceeat.demo.domain.dietScoreResult.entity.DietScoreResult;
import com.balanceeat.demo.domain.diet.dto.ai.UserDietDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.balanceeat.demo.util.RDICalculator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneTimeChatService {
    
    private final ChatClient chatClient;
    private final PromptChatMemoryAdvisor memoryAdvisor;
    private final ObjectMapper objectMapper;
    
    /**
     * ChatGPT 단건 호출 – 블로킹
     */
    public Mono<DietScoreResult> ask(UserDietDTO dto) {
        String cid = "diet-analysis-" + dto.getUserResponseDTO().getEmail() + dto.getDietSummaryDTO().getSummaryDate().toString();
        
        return Mono.just(buildPrompt(dto))
            .flatMap(prompt -> Mono.fromCallable(() -> 
                chatClient.prompt()
                    .advisors(spec -> spec
                        .param(CONVERSATION_ID, cid)
                        .advisors(memoryAdvisor)
                    )
                    .user(prompt)
                    .call()
                    .content()
            ).subscribeOn(Schedulers.boundedElastic()))
            .flatMap(content -> {
                if (content.trim().startsWith("{")) {
                    return Mono.fromCallable(() -> objectMapper.readValue(content, DietScoreResult.class))
                        .subscribeOn(Schedulers.boundedElastic());
                }
                String[] lines = content.split("\\n", 2);
                return Mono.just(DietScoreResult.builder()
                    .score(Integer.parseInt(lines[0].trim()))
                    .feedback(lines[1].trim())
                    .build());
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests)
                .doBeforeRetry(retrySignal -> 
                    log.warn("OpenAI API 요청 제한으로 인한 재시도. 시도 횟수: {}", retrySignal.totalRetries() + 1))
            )
            .doOnError(e -> log.error("AI 분석 중 오류 발생: {}", e.getMessage()))
            .onErrorResume(WebClientResponseException.TooManyRequests.class, e -> {
                log.error("OpenAI API 요청 제한 초과: {}", e.getMessage());
                return Mono.error(new RuntimeException("잠시 후 다시 시도해주세요. (요청 제한 초과)"));
            });
    }
    
    private String buildPrompt(UserDietDTO dto) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("당신은 영양사입니다. 다음 식단을 분석해주세요.\n\n");

        // 사용자의 RDI 정보 추가
        RDICalculator.RDIResult rdi = RDICalculator.calculateRDI(
            dto.getUserResponseDTO().getBirthYear(),
            dto.getUserResponseDTO().getWeight(),
            dto.getUserResponseDTO().getHeight(),
            dto.getUserResponseDTO().getGender().equals("MALE") ? RDICalculator.Gender.MALE : RDICalculator.Gender.FEMALE
        );

        prompt.append("[사용자 정보]\n");
        prompt.append(String.format("나이: %d세\n", 2024 - dto.getUserResponseDTO().getBirthYear()));
        prompt.append(String.format("체중: %dkg\n", dto.getUserResponseDTO().getWeight()));
        prompt.append(String.format("키: %dcm\n", dto.getUserResponseDTO().getHeight()));
        prompt.append(String.format("성별: %s\n", dto.getUserResponseDTO().getGender().equals("MALE") ? "남성" : "여성"));
        prompt.append(String.format("질환: %s\n", dto.getUserResponseDTO().getDiseaseCode()));
        prompt.append(String.format("식습관: %s\n", dto.getUserResponseDTO().getDietHabit()));
        prompt.append(String.format("음식 선호: %s\n\n", dto.getUserResponseDTO().getFoodPreference()));

        prompt.append("[일일 권장 섭취량]\n");
        prompt.append(String.format("총 칼로리: %dkcal\n", rdi.getTotalCalories()));
        prompt.append(String.format("탄수화물: %dg (권장 비율: 48%%)\n", rdi.getCarbohydrates()));
        prompt.append(String.format("단백질: %dg (권장 비율: 20%%)\n", rdi.getProtein()));
        prompt.append(String.format("지방: %dg (권장 비율: 32%%)\n\n", rdi.getFat()));

        prompt.append("[식단 정보]\n");
        prompt.append(String.format("총 칼로리: %.1fkcal (권장 대비 %.1f%%)\n", 
            dto.getDietSummaryDTO().getTotalCalories(), 
            (dto.getDietSummaryDTO().getTotalCalories() / rdi.getTotalCalories()) * 100));
        prompt.append(String.format("탄수화물: %.1fg (%.1f%%)\n", 
            dto.getDietSummaryDTO().getTotalCarbohydrates(), 
            (dto.getDietSummaryDTO().getTotalCarbohydrates() * 4 / dto.getDietSummaryDTO().getTotalCalories()) * 100));
        prompt.append(String.format("단백질: %.1fg (%.1f%%)\n", 
            dto.getDietSummaryDTO().getTotalProtein(), 
            (dto.getDietSummaryDTO().getTotalProtein() * 4 / dto.getDietSummaryDTO().getTotalCalories()) * 100));
        prompt.append(String.format("지방: %.1fg (%.1f%%)\n\n", 
            dto.getDietSummaryDTO().getTotalFat(), 
            (dto.getDietSummaryDTO().getTotalFat() * 9 / dto.getDietSummaryDTO().getTotalCalories()) * 100));

        prompt.append("[식단 상세]\n");
        prompt.append(String.format("아침: %.1fkcal\n", dto.getDietSummaryDTO().getBreakfastCalories()));
        prompt.append(String.format("점심: %.1fkcal\n", dto.getDietSummaryDTO().getLunchCalories()));
        prompt.append(String.format("저녁: %.1fkcal\n", dto.getDietSummaryDTO().getDinnerCalories()));
        prompt.append(String.format("간식: %.1fkcal\n", dto.getDietSummaryDTO().getSnackCalories()));
        prompt.append(String.format("야식: %.1fkcal\n\n", dto.getDietSummaryDTO().getNightCalories()));

        prompt.append("[분석 요청]\n");
        prompt.append("1. 일일 권장 섭취량 대비 현재 식단의 적절성을 평가해주세요.\n");
        prompt.append("2. 칼로리와 영양소 비율이 적절한지 분석해주세요.\n");
        prompt.append("3. 개선이 필요한 부분이 있다면 구체적인 제안을 해주세요.\n");
        prompt.append("4. 100점 만점으로 점수를 매겨주세요.\n");
        prompt.append("5. 분석 결과는 JSON 형식으로 응답해주세요. 형식은 다음과 같습니다:\n");
        prompt.append("{\n");
        prompt.append("  \"score\": 점수,\n");
        prompt.append("  \"feedback\": \"상세한 피드백\",\n");
        prompt.append("  \"calorieAnalysis\": \"칼로리 분석\",\n");
        prompt.append("  \"nutrientAnalysis\": \"영양소 분석\",\n");
        prompt.append("  \"suggestions1\": \"개선 제안1\",\n");
        prompt.append("  \"suggestions2\": \"개선 제안2\",\n");
        prompt.append("  \"suggestions3\": \"개선 제안3\"\n");
        prompt.append("}");

        return prompt.toString();
    }
}
