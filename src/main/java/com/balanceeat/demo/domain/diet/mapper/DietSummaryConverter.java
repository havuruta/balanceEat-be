package com.balanceeat.demo.domain.diet.mapper;

import com.balanceeat.demo.domain.diet.dto.DietSummaryDTO;
import com.balanceeat.demo.domain.diet.entity.DietSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DietSummaryConverter {
    
    @Mapping(target = "id", ignore = true)
    DietSummary toEntity(DietSummaryDTO dto);

    DietSummaryDTO toDTO(DietSummary entity);

    List<DietSummaryDTO> toDTOList(List<DietSummary> entities);
}