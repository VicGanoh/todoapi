package com.vicgan.todoapi.services.impl;

import com.vicgan.todoapi.dtos.CreateTaskDto;
import com.vicgan.todoapi.dtos.TaskDto;
import com.vicgan.todoapi.dtos.UpdateTaskDto;
import com.vicgan.todoapi.entities.Task;
import com.vicgan.todoapi.entities.User;
import com.vicgan.todoapi.exception.AlreadyExistsException;
import com.vicgan.todoapi.exception.NotFoundException;
import com.vicgan.todoapi.repositories.TaskRepository;
import com.vicgan.todoapi.repositories.UserRepository;
import com.vicgan.todoapi.services.TaskService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public TaskDto createTask(CreateTaskDto createTaskDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Task task  = new Task();
        task.setDescription(createTaskDto.getDescription());
        task.setCompleted(createTaskDto.isCompleted());
        task.setUser(user);
        task.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        task.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        taskRepository.save(task);

        logger.info("Invoking createTask() method inside TaskServiceImpl...");
        return modelMapper.map(task, TaskDto.class);
    }

    @Override
    @Transactional
    public TaskDto getTaskById(String id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()){
            Task task = taskOptional.get();

            logger.info("Invoking getTaskById() method inside TaskServiceImpl...");
            return modelMapper.map(task, TaskDto.class);
        }else {
            throw new NotFoundException("Task does not exist.");
        }
    }

    @Override
    @Transactional
    public Page<Task> getAllTasks(Specification<Task> taskSpec, Pageable pageable) {
        logger.info("Invoking getAllTasks() method inside TaskServiceImpl...");
        return taskRepository.findAll(taskSpec, pageable);
    }

    @Override
    @Transactional
    public TaskDto updateTask(UpdateTaskDto updateTaskDto, String email) {
        Optional<Task> taskOptional = taskRepository.findById(updateTaskDto.getId());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));

        if (taskOptional.isPresent()){
            Task updateTask = taskOptional.get();

            updateTask.setId(updateTaskDto.getId());
            updateTask.setDescription(updateTaskDto.getDescription());
            updateTask.setCompleted(updateTaskDto.isCompleted());
            updateTask.setUser(user);

            taskRepository.save(updateTask);

            logger.info("Invoking updateTaskDto() method inside TaskServiceImpl...");
            return modelMapper.map(updateTask, TaskDto.class);
        } else {
            logger.error("Task does not exist inside TaskServiceImpl...");
            throw new NotFoundException("Task does not exist.");
        }
    }
}
