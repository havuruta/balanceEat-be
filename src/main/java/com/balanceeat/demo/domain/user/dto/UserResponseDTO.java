package com.balanceeat.demo.domain.user.dto;

import com.balanceeat.demo.domain.user.entity.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
	private String email;
	private String nickname;
	private int birthYear;
	private String gender;
	private int weight;
	private int height;
	private String diseaseCode;
	private String dietHabit;
	private String foodBlacklist;
	private String foodPreference;
	private LocalDate createDate;
}
