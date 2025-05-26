package com.balanceeat.demo.domain.user.service;

import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.dto.UserProfileDTO;
import com.balanceeat.demo.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDTO getUserById(Long id);
    boolean existsByEmail(String email);
    User findByEmail(String email);
    UserProfileDTO getUserProfile(Long id);
    UserProfileDTO getCurrentUserProfile(UserDetails userDetails);
    UserDTO updateUser(UserDTO userDto, UserDetails userDetails);
    void deleteUser(Long id);
}