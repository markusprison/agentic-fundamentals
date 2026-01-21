package com.taskmanagement.integration;

import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void testCreateAndRetrieveTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("Integration Test Task");
        newTask.setDescription("Testing CRUD operations");
        newTask.setStatus("TODO");
        newTask.setCompleted(false);

        // CREATE
        var response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        Long taskId = objectMapper.readTree(responseBody).get("id").asLong();

        // RETRIEVE
        mockMvc.perform(get("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Integration Test Task"));
    }

    @Test
    void testCreateUpdateDeleteTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("CRUD Test Task");
        newTask.setDescription("Original description");
        newTask.setStatus("TODO");
        newTask.setCompleted(false);

        // CREATE
        var createResponse = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andReturn();

        Long taskId = objectMapper.readTree(createResponse.getResponse().getContentAsString()).get("id").asLong();

        // UPDATE
        Task updateData = new Task();
        updateData.setTitle("Updated CRUD Test Task");
        updateData.setDescription("Updated description");
        updateData.setStatus("IN_PROGRESS");
        updateData.setCompleted(false);

        mockMvc.perform(put("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated CRUD Test Task"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        // DELETE
        mockMvc.perform(delete("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // VERIFY DELETION
        mockMvc.perform(get("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus("TODO");
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus("DONE");
        task2.setCompleted(true);

        // Create two tasks
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task2)))
                .andExpect(status().isCreated());

        // Get all tasks
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void testCompleteTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("Task to Complete");
        newTask.setDescription("This task will be completed");
        newTask.setStatus("TODO");
        newTask.setCompleted(false);

        var createResponse = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andReturn();

        Long taskId = objectMapper.readTree(createResponse.getResponse().getContentAsString()).get("id").asLong();

        // Mark as completed
        Task completeData = new Task();
        completeData.setCompleted(true);
        completeData.setStatus("DONE");

        mockMvc.perform(put("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(completeData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void testCreateTaskWithDueDate() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("Task with Due Date");
        newTask.setDescription("This task has a due date");
        newTask.setStatus("TODO");
        newTask.setCompleted(false);
        newTask.setDueDate(java.time.LocalDateTime.now().plusDays(7));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task with Due Date"))
                .andExpect(jsonPath("$.dueDate").exists());
    }

    @Test
    void testMultipleTaskCreation() throws Exception {
        // Create 5 tasks
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setTitle("Task " + i);
            task.setDescription("Description for task " + i);
            task.setStatus("TODO");
            task.setCompleted(false);

            mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task)))
                    .andExpect(status().isCreated());
        }

        // Verify all 5 tasks are in the database
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }
}
