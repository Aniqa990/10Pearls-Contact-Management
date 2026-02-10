package com.aniqa.contact_mgt.controller;

import com.aniqa.contact_mgt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        log.info("API: Change password");

        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        log.info("API: Logout user");


        return ResponseEntity.ok().build();
    }
}
