package com.balanceeat.demo.domain.user.service;

import com.balanceeat.demo.domain.user.dto.UserDTO;

public interface UserService {
    UserDTO getUserById(String userId);
    boolean existsByEmail(String email);
    void updateUser(UserDTO userDTO);
    void deleteUser(String userId);
    
    boolean findByEmail(String email);
}