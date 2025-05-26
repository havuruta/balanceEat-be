package com.balanceeat.demo.domain.user.service.impl;

import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.dto.UserProfileDTO;
import com.balanceeat.demo.domain.user.entity.User;
import com.balanceeat.demo.domain.user.exception.UserNotFoundException;
import com.balanceeat.demo.domain.user.mapper.UserMapper;
import com.balanceeat.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.balanceeat.demo.exception.auth.InvalidPasswordException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 현재 로그인한 사용자의 프로필 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getCurrentUserProfile(UserDetails userDetails) {
        User user = findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new UserNotFoundException();
        }
        return getUserProfile(user.getId());
    }

    // 사용자 고유번호로 사용자 정보 조회
    @Override
    public UserDTO getUserById(Long id) { // 컨트롤러가 Json에서 받은 문자열 데이터를 @PathVariable 을 통해 long 타입으로 변환하여 전달하기 때문에 String 대신 Long 으로 작성
        return UserDTO.from(userMapper.getUserById(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    // 사용자 이름 중복 확인 or 존재 확인 여부 체크
    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserProfileDTO getUserProfile(Long id) {
        UserProfileDTO profile = userMapper.getUserProfile(id);
        if (profile == null) {
            throw new UserNotFoundException();
        }
        return profile;
    }

    // 사용자 정보 수정
    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDto, UserDetails userDetails) {
        log.debug("회원 정보 수정 시작: email={}, nickname={}", userDetails.getUsername(), userDto.getNickname());
        
        User currentUser = findByEmail(userDetails.getUsername());
        if(currentUser == null || !currentUser.isActive()) {
            log.warn("사용자를 찾을 수 없거나 비활성화된 계정입니다: {}", userDetails.getUsername());
            throw new UserNotFoundException();
        }
        
        // 현재 비밀번호 검증
        log.debug("현재 비밀번호 검증 시작");
        if (!passwordEncoder.matches(userDto.getCurrentPassword(), currentUser.getPassword())) {
            log.warn("비밀번호가 일치하지 않습니다: {}", userDetails.getUsername());
            throw new InvalidPasswordException("현재 비밀번호가 일치하지 않습니다.");
        }
        log.debug("현재 비밀번호 검증 성공");

        // 현재 로그인한 사용자의 정보만 수정 가능
        userDto.setId(currentUser.getId());
        
        // 새 비밀번호가 있는 경우에만 비밀번호 업데이트
        if (userDto.getNewPassword() != null && !userDto.getNewPassword().isEmpty()) {
            log.debug("새 비밀번호 업데이트");
            userDto.setNewPassword(passwordEncoder.encode(userDto.getNewPassword()));
        }
        
        log.debug("회원 정보 업데이트 시작: id={}, nickname={}", userDto.getId(), userDto.getNickname());
        userMapper.updateUser(userDto);
        log.debug("회원 정보 업데이트 완료");
        
        return getUserById(currentUser.getId());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userMapper.getUserById(id);
        if(user == null || !user.isActive()) {
            throw new UserNotFoundException();
        }
        userMapper.deleteUser(id);
    }
} 