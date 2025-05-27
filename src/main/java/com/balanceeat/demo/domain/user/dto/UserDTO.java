package com.balanceeat.demo.domain.user.dto;

import com.balanceeat.demo.domain.user.entity.User;
import com.balanceeat.demo.domain.user.entity.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String nickname;

    // 누락된 프로필 정보 필드 추가
    private int birthYear;
    private Gender gender;
    private int weight;
    private int height;
    private String diseaseCode;
    private String dietHabit;
    private String foodBlacklist;
    private String foodPreference;
    private Boolean isChallengeEnabled;
    private String password;

    public static UserDTO from(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());

        // User 엔티티로부터 프로필 정보 필드 복사
        dto.setBirthYear(user.getBirthYear());
        dto.setGender(user.getGender());
        dto.setWeight(user.getWeight());
        dto.setHeight(user.getHeight());
        dto.setDiseaseCode(user.getDiseaseCode());
        dto.setDietHabit(user.getDietHabit());
        dto.setFoodBlacklist(user.getFoodBlacklist());
        dto.setFoodPreference(user.getFoodPreference());
        dto.setIsChallengeEnabled(user.isChallengeEnabled());
        dto.setPassword(user.getPassword());

        return dto;
    }
}