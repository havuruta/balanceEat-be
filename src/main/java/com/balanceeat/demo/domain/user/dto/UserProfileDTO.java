package com.balanceeat.demo.domain.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String goalMessage;
} 