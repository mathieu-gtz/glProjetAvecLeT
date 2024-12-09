package com.example.ProjectManager_springboot.services.admin;

import com.example.ProjectManager_springboot.entities.User;
import com.example.ProjectManager_springboot.enums.UserRole;
import com.example.ProjectManager_springboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final UserRepository userRepository;
    @Override
    public void changeUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setUserRole(UserRole.valueOf(newRole));
        userRepository.save(user);
    }
}
