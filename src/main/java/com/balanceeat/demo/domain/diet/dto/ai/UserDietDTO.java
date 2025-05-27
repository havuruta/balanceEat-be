package com.balanceeat.demo.domain.diet.dto.ai;

import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDietDTO {
	private UserResponseDTO userResponseDTO;
	private DietSummaryDTO dietSummaryDTO;
}
