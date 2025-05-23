package com.balanceeat.demo.domain.user.mapper;

import com.balanceeat.demo.domain.auth.dto.RegisterRequestDTO;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;
import com.balanceeat.demo.domain.user.entity.User;
import org.mapstruct.*;
	import org.springframework.security.crypto.password.PasswordEncoder;
	import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface RegisterConverter {
	
	/** DTO → Entity */
	@Mapping(target = "password",
		expression = "java(passwordEncoder.encode(src.getPassword()))")
	@Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
	@Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")   // 최초엔 null
	@Mapping(target = "isActive", constant = "true")   // 기본값을 true로 설정
	@Mapping(target = "id",        ignore = true)   // PK는 DB에서
	@BeanMapping(ignoreUnmappedSourceProperties = {"passwordConfirm"}) // DTO 필드 → 엔티티엔 없음
	User toEntity(RegisterRequestDTO src,
		@Context PasswordEncoder passwordEncoder);

    /* 역방향 변환 필요 시 아래처럼 추가
    RegisterResponseDTO toDto(User user);
    */
	UserResponseDTO toUserResponseDTO(User user);
}
