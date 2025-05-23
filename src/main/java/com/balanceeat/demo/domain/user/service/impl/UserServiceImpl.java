package com.balanceeat.demo.domain.user.service.impl;

import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.entity.User;
import com.balanceeat.demo.domain.user.exception.UserNotFoundException;
import com.balanceeat.demo.domain.user.mapper.UserMapper;
import com.balanceeat.demo.domain.user.service.UserService;
import com.balanceeat.demo.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    
    @Override
    public UserDTO getUserById(String userId) {
        log.debug("사용자 조회 시작: {}", userId);
        User user = userMapper.getUserById(userId);
        if (user == null) {
            log.warn("사용자를 찾을 수 없음: {}", userId);
            throw new UserNotFoundException();
        }
        log.debug("사용자 조회 완료: {}", userId);
        return UserDTO.from(user);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        log.debug("사용자명 존재 여부 확인: {}", email);
        boolean exists = userMapper.existsByEmail(email);
        log.debug("사용자명 존재 여부 확인 결과: {} - {}", email, exists);
        return exists;
    }
    
    @Override
    public void updateUser(UserDTO userDTO) {
        if (existsByEmail(userDTO.getEmail())) {
            throw new BusinessException("동일한 아이디가 있습니다. 다른 아이디로 변경하세요", HttpStatus.BAD_REQUEST);
        }
        userMapper.updateUser(userDTO);
    }
    
    @Override
    public void deleteUser(String userId) {
    
    }
    
    @Override
    public boolean findByEmail(String email) {
        log.debug("사용자명으로 조회 시작: {}", email);
        User user = userMapper.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
        if (user == null) {
            log.warn("사용자를 찾을 수 없음: {}", email);
            throw new UserNotFoundException();
        }
        log.debug("사용자명으로 조회 완료: {}", email);
        return true;
    }
} 