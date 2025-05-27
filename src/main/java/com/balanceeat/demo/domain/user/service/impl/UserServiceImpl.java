package com.balanceeat.demo.domain.user.service.impl;

import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.dto.UserProfileDTO;
import com.balanceeat.demo.domain.user.dto.UserResponseDTO;
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
    // 이미지 파일 저장 등을 위한 추가 서비스 또는 로직이 필요할 수 있습니다.
    // 예시로, 파일 업로드를 처리하는 별도의 서비스가 있다고 가정합니다.
    // private final FileStorageService fileStorageService; // 필요하다면 주입

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

    @Override
    public UserResponseDTO getUserResponseDTO(Long id) {
        UserResponseDTO userResponseDTO = userMapper.getUserResponseDTO(id);
        if (userResponseDTO == null) {
            throw new UserNotFoundException();
        }
        return userResponseDTO;
    }
    
    // 사용자 정보 수정 (기존 메소드 유지 - 비밀번호 변경 등 다른 용도로 사용될 수 있음)
    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDto, UserDetails userDetails) {
        log.debug("회원 정보 수정 시작: email={}, nickname={}", userDetails.getUsername(), userDto.getNickname());

        User currentUser = findByEmail(userDetails.getUsername());
        if(currentUser == null || !currentUser.isActive()) {
            log.warn("사용자를 찾을 수 없거나 비활성화된 계정입니다: {}", userDetails.getUsername());
            throw new UserNotFoundException();
        }

        // UserDTO에 현재 사용자의 최신 프로필 정보 값을 채워 넣습니다.
        // 이렇게 하면 프론트에서 보내지 않은 필드는 기존 값이 유지됩니다.
        UserDTO currentUserDto = UserDTO.from(currentUser);

        // 프론트에서 업데이트 요청한 필드만 currentUserDto에 복사
        if (userDto.getNickname() != null) currentUserDto.setNickname(userDto.getNickname());
        // birthYear는 0이 유효한 값일 수도 있으므로, 프론트에서 보낸 경우에만 업데이트 (0이 아닌 경우 또는 특정 기준으로 판단)
        if (userDto.getBirthYear() != 0) currentUserDto.setBirthYear(userDto.getBirthYear());
        if (userDto.getGender() != null) currentUserDto.setGender(userDto.getGender());
        if (userDto.getWeight() != 0) currentUserDto.setWeight(userDto.getWeight());
        if (userDto.getHeight() != 0) currentUserDto.setHeight(userDto.getHeight());
        if (userDto.getDiseaseCode() != null) currentUserDto.setDiseaseCode(userDto.getDiseaseCode());
        if (userDto.getDietHabit() != null) currentUserDto.setDietHabit(userDto.getDietHabit());
        if (userDto.getFoodBlacklist() != null) currentUserDto.setFoodBlacklist(userDto.getFoodBlacklist());
        if (userDto.getFoodPreference() != null) currentUserDto.setFoodPreference(userDto.getFoodPreference());
        // isChallengeEnabled는 boolean 타입이므로, UserDTO에 Boolean 객체로 정의되어 있다면 null 체크, 아니면 항상 업데이트 또는 다른 기준 적용
        // UserDTO의 isChallengeEnabled 필드가 boolean 원시 타입이라면, 프론트에서 보내지 않으면 기본값 false가 될 것입니다.
        // 프론트에서 명시적으로 값을 보낸 경우에만 업데이트하려면 별도의 필드 또는 로직이 필요합니다.
        // 현재는 userDto의 isChallengeEnabled 값을 그대로 사용한다고 가정합니다.
         currentUserDto.setIsChallengeEnabled(userDto.getIsChallengeEnabled());

        // 비밀번호 변경 처리
        // UserDTO에서 currentPassword와 newPassword를 가져와 비밀번호 변경 로직 수행
        if (userDto.getCurrentPassword() != null && !userDto.getCurrentPassword().isEmpty()) {
             log.debug("현재 비밀번호 검증 시작");
            if (!passwordEncoder.matches(userDto.getCurrentPassword(), currentUser.getPassword())) {
                log.warn("비밀번호가 일치하지 않습니다: {}", userDetails.getUsername());
                throw new InvalidPasswordException("현재 비밀번호가 일치하지 않습니다.");
            }
            log.debug("현재 비밀번호 검증 성공");

             // 새 비밀번호가 있는 경우에만 비밀번호 업데이트
            if (userDto.getNewPassword() != null && !userDto.getNewPassword().isEmpty()) {
                log.debug("새 비밀번호 업데이트");
                // User 엔티티의 비밀번호 업데이트 후 currentUserDto에 반영
                currentUser.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                 currentUserDto.setPassword(currentUser.getPassword()); // currentUserDto에도 업데이트된 비밀번호 설정
            }
        }

        // 현재 로그인한 사용자의 정보만 수정 가능 (id 설정)
        currentUserDto.setId(currentUser.getId());

        // MyBatis 매퍼에 현재 사용자의 최신 정보와 업데이트할 필드 값이 반영된 DTO 전달
        userMapper.updateUser(currentUserDto);

        log.debug("회원 정보 업데이트 완료");

        // 업데이트 후 최신 UserDTO 반환
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

     // 프로필 이미지 저장 로직 (구현 필요)
    // private String saveProfileImage(Long userId, MultipartFile imageFile) {
    //     // 파일 저장 로직 구현: 파일 시스템에 저장하거나 클라우드 스토리지에 업로드
    //     // 저장된 파일의 접근 가능한 URL 반환
    //     return "saved_image_url";
    // }

}