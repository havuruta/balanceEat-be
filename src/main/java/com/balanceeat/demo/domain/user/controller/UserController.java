package com.balanceeat.demo.domain.user.controller;

import com.balanceeat.demo.domain.user.dto.UserDTO;
import com.balanceeat.demo.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 마이페이지 조회
    @GetMapping("/mypage")
    public String mypage(@SessionAttribute("user") Map<String, String> userInfo, Model model) {
        String userId = userInfo.get("id");
        UserDTO user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user/mypage";
    }
    
    // 사용자 정보 수정 페이지
    @GetMapping("/update")
    public String updateForm(@SessionAttribute("user") Map<String, String> userInfo, Model model) {
        String userId = userInfo.get("id");
        UserDTO user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user/updateUser";
    }
    
    // 사용자 정보 수정 처리
    @PostMapping("/update")
    public String updateUser(@ModelAttribute UserDTO userDTO) {
        userService.updateUser(userDTO);
        return "redirect:/user/mypage";
    }
    
    // 회원 탈퇴 페이지
    @GetMapping("/delete")
    public String deleteForm() {
        return "user/deleteUser";
    }
    
    // 회원 탈퇴 처리
    @PostMapping("/delete")
    public String deleteUser(@SessionAttribute("user") Map<String, String> userInfo) {
        String userId = userInfo.get("id");
        userService.deleteUser(userId);
        return "redirect:/auth/logout";
    }
} 