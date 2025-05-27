package com.balanceeat.demo.ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequestDTO {
    private String content;
    private Integer month;
    private Integer week;
} 