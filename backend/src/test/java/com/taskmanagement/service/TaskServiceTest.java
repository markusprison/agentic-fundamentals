package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Test Task 1");
        task1.setDescription("Description 1");
        task1.setCompleted(false);
        task1.setStatus("TODO");

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Test Task 2");
        task2.setDescription("Description 2");
        task2.setCompleted(true);
        task2.setStatus("DONE");
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Test Task 1", result.get(0).getTitle());
        assertEquals("Test Task 2", result.get(1).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTasksEmpty() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList());

        List<Task> result = taskService.getAllTasks();

        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Task 1", result.get().getTitle());
        assertEquals("Description 1", result.get().getDescription());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTaskById(999L);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateTask() {
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setStatus("TODO");
        newTask.setCompleted(false);

        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        Task result = taskService.createTask(newTask);

        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals("TODO", result.getStatus());
        assertFalse(result.getCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTaskSuccess() {
        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Task");
        taskDetails.setDescription("Updated Description");
        taskDetails.setCompleted(true);
        taskDetails.setStatus("DONE");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(taskDetails);

        Task result = taskService.updateTask(1L, taskDetails);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertTrue(result.getCompleted());
        assertEquals("DONE", result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTaskPartial() {
        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Title Only");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(1L, taskDetails);

        assertEquals("Updated Title Only", result.getTitle());
        assertEquals("Description 1", result.getDescription());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTaskNotFound() {
        Task taskDetails = new Task();
        taskDetails.setTitle("Updated");

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.updateTask(999L, taskDetails));
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteTaskSuccess() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        doNothing().when(taskRepository).delete(any(Task.class));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.deleteTask(999L));
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateMultipleTasks() {
        List<Task> newTasks = Arrays.asList(task1, task2);
        when(taskRepository.save(any(Task.class)))
                .thenReturn(task1)
                .thenReturn(task2);

        for (Task task : newTasks) {
            taskService.createTask(task);
        }

        verify(taskRepository, times(2)).save(any(Task.class));
    }
}
