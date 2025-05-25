package com.balanceeat.demo.domain.diet.mapper;

import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DietSummaryConverter {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    DietSummary toEntity(DietSummaryDTO dto);

    DietSummaryDTO toDTO(DietSummary entity);

    List<DietSummaryDTO> toDTOList(List<DietSummary> entities);

    default DietSummary updateEntityFromDTO(DietSummaryDTO dto, DietSummary entity) {
        DietSummary updated = toEntity(dto);
        return updated.withId(entity.getId())
                     .withCreatedAt(entity.getCreatedAt())
                     .withUpdatedAt(LocalDateTime.now());
    }
} 