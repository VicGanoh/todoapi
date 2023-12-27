package com.vicgan.todoapi.controller.v1;

import com.vicgan.todoapi.dtos.CreateTaskDto;
import com.vicgan.todoapi.dtos.TaskDto;
import com.vicgan.todoapi.dtos.UpdateTaskDto;
import com.vicgan.todoapi.entities.Task;
import com.vicgan.todoapi.queryspec.ListTaskSpec;
import com.vicgan.todoapi.response.ApiResponse;
import com.vicgan.todoapi.services.impl.TaskServiceImpl;
import com.vicgan.todoapi.utils.IsUser;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaskServiceImpl taskService;

    @PostMapping
    @IsUser
    public ApiResponse<TaskDto> createTask(@Valid @RequestBody CreateTaskDto createTaskDto, Authentication authentication){
        String userEmail = authentication.getName();
        logger.info("HTTP Request: createTask(): {}", createTaskDto);
        TaskDto taskDtoResponse = taskService.createTask(createTaskDto, userEmail);

        logger.info("HTTP Response: createTask(): {}", taskDtoResponse);
        return new ApiResponse<>(taskDtoResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    @IsUser
    public ApiResponse<TaskDto> getTaskById(@PathVariable String id){
        logger.info("HTTP Request: getTaskById(): {}", id);

        TaskDto taskDtoResponse = taskService.getTaskById(id);

        logger.info("HTTP Response: getTaskById(): {}", taskDtoResponse);
        return new ApiResponse<>(taskDtoResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    @IsUser
    public ApiResponse<List<TaskDto>> getAllTasks(ListTaskSpec taskSpec, Pageable pageable){
        logger.info("HTTP Request: getAllTasks()");
        pageable = PageRequest.of(0, 5);
        Page<Task> taskPage = taskService.getAllTasks(taskSpec, pageable);

        List<TaskDto> taskDtosResponse = taskPage.stream().map(task -> modelMapper.map(task, TaskDto.class)).collect(Collectors.toList());


        logger.info("HTTP Response: getAllTasks(): {}", taskDtosResponse);
        return new ApiResponse<>(taskDtosResponse, HttpStatus.OK);
    }

    @PutMapping
    public ApiResponse<TaskDto> updateTask(@RequestBody UpdateTaskDto updateTaskDto, Authentication authentication){
        logger.info("HTTP Request: updateTaskById(): {}", updateTaskDto);
        String userEmail = authentication.getName();
        TaskDto taskDtoResponse = taskService.updateTask(updateTaskDto, userEmail);

        logger.info("HTTP Request: updateTaskById(): {}", taskDtoResponse);
        return new ApiResponse<>(taskDtoResponse, HttpStatus.OK);
    }

}
