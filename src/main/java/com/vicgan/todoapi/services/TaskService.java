package com.vicgan.todoapi.services;

import com.vicgan.todoapi.dtos.CreateTaskDto;
import com.vicgan.todoapi.dtos.TaskDto;
import com.vicgan.todoapi.dtos.UpdateTaskDto;
import com.vicgan.todoapi.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface TaskService {

    TaskDto createTask(CreateTaskDto createTaskDto, String email);

    TaskDto getTaskById(String id);

    Page<Task> getAllTasks(Specification<Task> taskSpec, Pageable pageable);

    TaskDto updateTask(UpdateTaskDto updateTaskDto, String email);
}
