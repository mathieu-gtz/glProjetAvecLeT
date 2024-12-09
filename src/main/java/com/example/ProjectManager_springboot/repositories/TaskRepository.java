package com.example.ProjectManager_springboot.repositories;

import com.example.ProjectManager_springboot.dto.TaskDto;
import com.example.ProjectManager_springboot.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByTitleContaining(String title);

    List<TaskDto> findAllByProjectId(Long projectId);

}
