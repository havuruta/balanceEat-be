package com.balanceeat.demo.domain.user.mapper;

import java.util.Optional;

import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getUserById(String id);
    void updateUser(UserDTO userDTO);
    void deleteUser(String id);
    Optional<User> findByEmail(String email);
    void insert(User user);
    void updateUserIsActive(String id, boolean isActive);
    boolean existsByEmail(String email);
}