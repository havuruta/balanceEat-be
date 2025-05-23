package com.balanceeat.demo.domain.user.dto;

import com.balanceeat.demo.domain.user.entity.User;

import lombok.Getter;

@Getter
public class UserDTO {
    private String id;
    private String email;
    private String password;
	
	public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        return userDTO;
	}
}