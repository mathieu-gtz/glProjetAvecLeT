package com.example.ProjectManager_springboot.services.manager;

import com.example.ProjectManager_springboot.dto.ProjectDto;
import com.example.ProjectManager_springboot.dto.TaskDto;
import com.example.ProjectManager_springboot.entities.Project;
import com.example.ProjectManager_springboot.entities.Task;
import com.example.ProjectManager_springboot.entities.User;
import com.example.ProjectManager_springboot.enums.TaskStatus;
import com.example.ProjectManager_springboot.repositories.ProjectRepository;
import com.example.ProjectManager_springboot.repositories.TaskRepository;
import com.example.ProjectManager_springboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        User manager = userRepository.findById(projectDto.getManagerId()).orElse(null);
        if (manager == null) {
            return null; // Handle manager not found
        }

        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setManager(manager);

        project = projectRepository.save(project);
        projectDto.setId(project.getId());
        return projectDto;

    }

    @Override
    public TaskDto createTask(Long projectId, TaskDto taskDto) {
        Optional<User> optionalUser = userRepository.findById(taskDto.getEmployee());
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalUser.isPresent() && optionalProject.isPresent()) {
            Task task = new Task();
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setPriority(taskDto.getPriority());
            task.setDueDate(taskDto.getDueDate());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            task.setUser(optionalUser.get());
            task.setProject(optionalProject.get());
            return taskRepository.save(task).getTaskDto();
        }
        return null;
    }


    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<ProjectDto> getProjectsByManager(Long managerId) {
        List<Project> projects = projectRepository.findByManagerId(managerId);
        return projects.stream().map(Project::getProjectDto).collect(Collectors.toList());
    }


    @Override
    public List<TaskDto> getTasksByProjectId(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            return project.get().getTasks().stream()
                    .map(Task::getTaskDto)
                    .collect(Collectors.toList());
        }
        return null;
    }


    @Override
    public TaskDto updateTask(Long id, TaskDto taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployee());
        if (optionalTask.isPresent() && optionalUser.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setPriority(taskDTO.getPriority());
            existingTask.setTaskStatus(mapStringToStatus(String.valueOf(taskDTO.getTaskStatus())));
            existingTask.setUser(optionalUser.get());
            return taskRepository.save(existingTask).getTaskDto();
        }
        return null;
    }

    private TaskStatus mapStringToStatus(String status){
        switch (status){
            case "PENDING":
                return TaskStatus.PENDING;
            case "INPROGRESS":
                return TaskStatus.INPROGRESS;
            case "COMPLETED":
                return TaskStatus.COMPLETED;
            case "DEFERRED":
                return TaskStatus.DEFERRED;
            default:
                return TaskStatus.CANCELED;
        }
    }


}
