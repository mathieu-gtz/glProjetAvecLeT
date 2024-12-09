package com.example.ProjectManager_springboot.services.admin;

public interface AdminService {
    void changeUserRole(Long userId, String newRole);
}
