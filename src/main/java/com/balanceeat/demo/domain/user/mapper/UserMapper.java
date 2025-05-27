package com.balanceeat.demo.domain.user.mapper;

import java.util.Optional;

import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.dto.UserProfileDTO;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;
import com.balanceeat.demo.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getUserById(Long id);
    UserProfileDTO getUserProfile(Long id);
    UserResponseDTO getUserResponseDTO(Long id);
    void updateUser(UserDTO userDTO);
    void deleteUser(Long id);
    Optional<User> findByEmail(String email);
    void insert(User user);
    boolean existsByEmail(String email);
}