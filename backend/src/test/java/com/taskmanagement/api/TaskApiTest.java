package com.taskmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
    }

    // ==================== GET /api/tasks ====================

    @Test
    public void testGetAllTasks_EmptyList() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetAllTasks_WithMultipleTasks() throws Exception {
        // Create test tasks
        Task task1 = new Task();
        task1.setTitle("Test Task 1");
        task1.setDescription("Description 1");
        task1.setStatus("TODO");
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Test Task 2");
        task2.setDescription("Description 2");
        task2.setStatus("IN_PROGRESS");
        taskRepository.save(task2);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test Task 1")))
                .andExpect(jsonPath("$[1].title", is("Test Task 2")))
                .andExpect(jsonPath("$[0].status", is("TODO")))
                .andExpect(jsonPath("$[1].status", is("IN_PROGRESS")));
    }

    // ==================== GET /api/tasks/{id} ====================

    @Test
    public void testGetTaskById_Success() throws Exception {
        Task task = new Task();
        task.setTitle("Get Task Test");
        task.setDescription("Test Description");
        task.setStatus("TODO");
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedTask.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Get Task Test")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.status", is("TODO")));
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    // ==================== POST /api/tasks ====================

    @Test
    public void testCreateTask_Success() throws Exception {
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Description");
        task.setStatus("TODO");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.description", is("New Description")))
                .andExpect(jsonPath("$.status", is("TODO")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void testCreateTask_EmptyTitle() throws Exception {
        Task task = new Task();
        task.setTitle("");
        task.setDescription("Description");
        task.setStatus("TODO");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Task title cannot be empty")));
    }

    @Test
    public void testCreateTask_NullTitle() throws Exception {
        String taskJson = "{\"description\": \"Test\", \"status\": \"TODO\"}";

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateTask_WithAllFields() throws Exception {
        Task task = new Task();
        task.setTitle("Complete Task");
        task.setDescription("Full description with all fields");
        task.setStatus("DONE");
        task.setCompleted(true);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Complete Task")))
                .andExpect(jsonPath("$.description", is("Full description with all fields")))
                .andExpect(jsonPath("$.status", is("DONE")))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    // ==================== PUT /api/tasks/{id} ====================

    @Test
    public void testUpdateTask_Success() throws Exception {
        Task originalTask = new Task();
        originalTask.setTitle("Original Title");
        originalTask.setDescription("Original Description");
        originalTask.setStatus("TODO");
        Task savedTask = taskRepository.save(originalTask);

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus("DONE");
        updatedTask.setCompleted(true);

        mockMvc.perform(put("/api/tasks/" + savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedTask.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.status", is("DONE")))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Test
    public void testUpdateTask_NotFound() throws Exception {
        Task task = new Task();
        task.setTitle("Test");
        task.setDescription("Test");
        task.setStatus("TODO");

        mockMvc.perform(put("/api/tasks/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTask_EmptyTitle() throws Exception {
        Task originalTask = new Task();
        originalTask.setTitle("Original");
        originalTask.setDescription("Description");
        originalTask.setStatus("TODO");
        Task savedTask = taskRepository.save(originalTask);

        Task updatedTask = new Task();
        updatedTask.setTitle("");
        updatedTask.setDescription("New Description");
        updatedTask.setStatus("DONE");

        mockMvc.perform(put("/api/tasks/" + savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Task title cannot be empty")));
    }

    @Test
    public void testUpdateTask_PartialUpdate() throws Exception {
        Task originalTask = new Task();
        originalTask.setTitle("Original Title");
        originalTask.setDescription("Original Description");
        originalTask.setStatus("TODO");
        Task savedTask = taskRepository.save(originalTask);

        Task partialUpdate = new Task();
        partialUpdate.setTitle("Partially Updated Title");
        partialUpdate.setDescription("Original Description");
        partialUpdate.setStatus("TODO");

        mockMvc.perform(put("/api/tasks/" + savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Partially Updated Title")))
                .andExpect(jsonPath("$.description", is("Original Description")));
    }

    // ==================== DELETE /api/tasks/{id} ====================

    @Test
    public void testDeleteTask_Success() throws Exception {
        Task task = new Task();
        task.setTitle("Task to Delete");
        task.setDescription("Will be deleted");
        task.setStatus("TODO");
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(delete("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isNoContent());

        // Verify task is deleted
        mockMvc.perform(get("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTask_VerifyListEmpty() throws Exception {
        Task task = new Task();
        task.setTitle("Task");
        task.setDescription("Description");
        task.setStatus("TODO");
        Task savedTask = taskRepository.save(task);

        // Verify task exists
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Delete task
        mockMvc.perform(delete("/api/tasks/" + savedTask.getId()))
                .andExpect(status().isNoContent());

        // Verify list is empty
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ==================== Status Codes & Content-Type Tests ====================

    @Test
    public void testCreateTask_ContentTypeJSON() throws Exception {
        Task task = new Task();
        task.setTitle("Content Type Test");
        task.setDescription("Testing content type");
        task.setStatus("TODO");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllTasks_ContentTypeJSON() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ==================== CORS Tests ====================

    @Test
    public void testCORSHeaders_OnGetRequest() throws Exception {
        mockMvc.perform(get("/api/tasks")
                .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk());
    }

    // ==================== Edge Cases ====================

    @Test
    public void testCreateTask_WithMaxLengthTitle() throws Exception {
        Task task = new Task();
        task.setTitle("a".repeat(100)); // Max length for title
        task.setDescription("Description");
        task.setStatus("TODO");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", hasLength(100)));
    }

    @Test
    public void testCreateTask_WithMaxLengthDescription() throws Exception {
        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("d".repeat(500)); // Max length for description
        task.setStatus("TODO");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", hasLength(500)));
    }

    @Test
    public void testCreateTask_WithWhitespaceTitle() throws Exception {
        Task task = new Task();
        task.setTitle("   "); // Only whitespace
        task.setDescription("Description");
        task.setStatus("TODO");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Task title cannot be empty")));
    }

    @Test
    public void testGetTaskById_WithInvalidId() throws Exception {
        mockMvc.perform(get("/api/tasks/invalid"))
                .andExpect(status().isBadRequest());
    }

    // ==================== Multiple Operations ====================

    @Test
    public void testCreateMultipleTasks_ThenRetrieveAll() throws Exception {
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setTitle("Task " + i);
            task.setDescription("Description " + i);
            task.setStatus("TODO");

            mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(task)))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void testCreateUpdateAndDelete_Workflow() throws Exception {
        // Create task
        Task task = new Task();
        task.setTitle("Workflow Task");
        task.setDescription("Original");
        task.setStatus("TODO");

        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = createResult.getResponse().getContentAsString();
        Long taskId = objectMapper.readTree(response).get("id").asLong();

        // Update task
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Workflow Task");
        updatedTask.setDescription("Updated");
        updatedTask.setStatus("IN_PROGRESS");

        mockMvc.perform(put("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Workflow Task")));

        // Delete task
        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }
}
