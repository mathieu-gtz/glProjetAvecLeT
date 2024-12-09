package com.example.ProjectManager_springboot.controller.admin;


import com.example.ProjectManager_springboot.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/change-role/{userId}")
    public ResponseEntity<?> changeUserRole(@PathVariable Long userId, @RequestParam String newRole) {
        adminService.changeUserRole(userId, newRole);
        return ResponseEntity.ok("User role updated successfully");
    }
}
