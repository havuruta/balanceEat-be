package com.balanceeat.demo.domain.nutrition.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponseDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static <T> PageResponseDTO<T> of(List<T> content, int page, int size, long totalElements) {
        PageResponseDTO<T> response = new PageResponseDTO<>();
        response.setContent(content);
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(totalElements);
        response.setTotalPages((int) Math.ceil((double) totalElements / size));
        return response;
    }
} 