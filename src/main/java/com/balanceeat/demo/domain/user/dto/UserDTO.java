package com.balanceeat.demo.domain.user.dto;

import com.balanceeat.demo.domain.user.entity.User;
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

    public static UserDTO from(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        return dto;
    }
}