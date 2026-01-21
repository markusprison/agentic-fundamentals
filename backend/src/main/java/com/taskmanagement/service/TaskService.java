package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        validateTaskTitle(task.getTitle());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (taskDetails.getTitle() != null) {
            validateTaskTitle(taskDetails.getTitle());
            task.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getDescription() != null) {
            task.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getCompleted() != null) {
            task.setCompleted(taskDetails.getCompleted());
        }
        if (taskDetails.getStatus() != null) {
            task.setStatus(taskDetails.getStatus());
        }
        if (taskDetails.getDueDate() != null) {
            task.setDueDate(taskDetails.getDueDate());
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        taskRepository.delete(task);
    }

    private void validateTaskTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
    }
}
