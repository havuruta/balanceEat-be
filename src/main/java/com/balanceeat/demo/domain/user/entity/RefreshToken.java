package com.balanceeat.demo.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshToken {
	private String key;
	private String value;
	
	@Builder
	public static RefreshToken create(String key, String value) {
		return new RefreshToken(key, value);
	}
}
