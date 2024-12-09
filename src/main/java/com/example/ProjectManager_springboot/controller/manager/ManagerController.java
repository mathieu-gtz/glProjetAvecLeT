package com.example.ProjectManager_springboot.controller.manager;

import com.example.ProjectManager_springboot.dto.ProjectDto;
import com.example.ProjectManager_springboot.dto.TaskDto;
import com.example.ProjectManager_springboot.entities.Project;
import com.example.ProjectManager_springboot.enums.TaskStatus;
import com.example.ProjectManager_springboot.repositories.ProjectRepository;
import com.example.ProjectManager_springboot.services.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {
    private final ManagerService managerService;
    private final ProjectRepository projectRepository;

    @PostMapping("/projects")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectDto createdProject = managerService.createProject(projectDto);
        if (createdProject == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PostMapping("/project/{projectId}/tasks")
    public ResponseEntity<TaskDto> createTask(@PathVariable Long projectId, @RequestBody TaskDto taskDto) {
        TaskDto createdTaskDTO = managerService.createTask(projectId, taskDto);
        if (createdTaskDTO == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDTO);
    }


    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        managerService.deleteTask(id);
        return ResponseEntity.ok(null);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/{managerId}/projects")
    public ResponseEntity<List<ProjectDto>> getProjectsByManager(@PathVariable Long managerId) {
        List<ProjectDto> projects = managerService.getProjectsByManager(managerId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/project/{projectId}/manager")
    public ResponseEntity<Long> getManagerIdByProject(@PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null || project.getManager() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(project.getManager().getId());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskDto>> viewProject(@PathVariable Long projectId) {
        List<TaskDto> tasks = managerService.getTasksByProjectId(projectId);
        if (tasks == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDTO) {
        TaskDto updatedTask = managerService.updateTask(id, taskDTO);
        if (updatedTask == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedTask);
    }

}
