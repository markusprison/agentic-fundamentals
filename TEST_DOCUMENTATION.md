# Task Management Application - Test Suite Documentation

This document provides an overview of the comprehensive test suites created for both the backend and frontend of the Task Management Application.

## Backend Tests (Java/Spring Boot)

### Overview
The backend tests cover unit tests, service tests, and integration tests using JUnit, Mockito, and Spring Boot Test framework.

### Test Files

#### 1. TaskControllerTest.java
**Location**: `backend/src/test/java/com/taskmanagement/controller/TaskControllerTest.java`

Tests the REST API endpoints for CRUD operations:

- **testGetAllTasks()**: Verifies fetching all tasks returns correct list
- **testGetTaskById()**: Verifies getting a specific task by ID
- **testGetTaskByIdNotFound()**: Verifies 404 response when task doesn't exist
- **testCreateTask()**: Tests POST request to create new task
- **testUpdateTask()**: Tests PUT request to update existing task
- **testUpdateTaskNotFound()**: Verifies 404 when updating non-existent task
- **testDeleteTask()**: Tests DELETE request to remove task
- **testDeleteTaskNotFound()**: Verifies 404 when deleting non-existent task

**Running the test**:
```bash
cd backend
mvn test -Dtest=TaskControllerTest
```

#### 2. TaskServiceTest.java
**Location**: `backend/src/test/java/com/taskmanagement/service/TaskServiceTest.java`

Unit tests for business logic in TaskService:

- **testGetAllTasks()**: Verifies retrieving all tasks from repository
- **testGetAllTasksEmpty()**: Tests behavior with empty task list
- **testGetTaskById()**: Tests retrieval of single task
- **testGetTaskByIdNotFound()**: Tests Optional.empty() response
- **testCreateTask()**: Tests task creation with proper data
- **testUpdateTaskSuccess()**: Tests partial and full update scenarios
- **testUpdateTaskPartial()**: Tests updating specific fields
- **testUpdateTaskNotFound()**: Tests exception handling for missing task
- **testDeleteTaskSuccess()**: Tests successful task deletion
- **testDeleteTaskNotFound()**: Tests exception for non-existent task
- **testCreateMultipleTasks()**: Tests creating multiple tasks sequentially

**Running the test**:
```bash
cd backend
mvn test -Dtest=TaskServiceTest
```

#### 3. TaskIntegrationTest.java
**Location**: `backend/src/test/java/com/taskmanagement/integration/TaskIntegrationTest.java`

End-to-end integration tests using real Spring context:

- **testCreateAndRetrieveTask()**: Tests CREATE and READ operations together
- **testCreateUpdateDeleteTask()**: Full CRUD workflow test
- **testGetAllTasks()**: Tests retrieving list of tasks
- **testCompleteTask()**: Tests marking task as completed
- **testCreateTaskWithDueDate()**: Tests task creation with due dates
- **testMultipleTaskCreation()**: Tests creating and retrieving multiple tasks

**Running the test**:
```bash
cd backend
mvn test -Dtest=TaskIntegrationTest
```

### Running All Backend Tests
```bash
cd backend
mvn clean test
```

---

## Frontend Tests (React with Vitest)

### Overview
Frontend tests use Vitest, React Testing Library, and axios-mock-adapter for testing React components and API interactions.

### Test Files

#### 1. App.test.jsx
**Location**: `frontend/src/test/App.test.jsx`

Integration tests for the main App component:

- **testRenderApp()**: Verifies App component renders correctly
- **testDisplayComponents()**: Checks TaskForm and TaskList are present
- **testFetchTasksOnMount()**: Tests fetching tasks on component load
- **testDisplayErrorOnFetchFailure()**: Tests error handling for failed API calls
- **testCreateNewTask()**: Tests adding a new task via form
- **testCreateTaskError()**: Tests error handling when creation fails
- **testUpdateTask()**: Tests updating existing task
- **testDeleteTask()**: Tests deleting a task
- **testDeleteTaskError()**: Tests error handling when deletion fails

**Running the test**:
```bash
cd frontend
npm test -- App.test.jsx
```

#### 2. TaskForm.test.jsx
**Location**: `frontend/src/test/TaskForm.test.jsx`

Component tests for TaskForm:

- **testRenderForm()**: Verifies form fields are rendered
- **testNoSubmitWithoutTitle()**: Validates required field validation
- **testSubmitFormData()**: Tests form submission with valid data
- **testClearFormAfterSubmission()**: Verifies form reset after submit
- **testTitleMaxLength()**: Tests 100 character limit on title
- **testDescriptionMaxLength()**: Tests 500 character limit on description
- **testStatusDropdown()**: Tests status field selection

**Running the test**:
```bash
cd frontend
npm test -- TaskForm.test.jsx
```

#### 3. TaskList.test.jsx
**Location**: `frontend/src/test/TaskList.test.jsx`

Component tests for TaskList:

- **testRenderTaskList()**: Verifies tasks are displayed
- **testEmptyState()**: Tests message when no tasks exist
- **testDeleteButton()**: Tests delete button functionality
- **testCompletedStatus()**: Tests displaying completed status

**Running the test**:
```bash
cd frontend
npm test -- TaskList.test.jsx
```

#### 4. TaskItem.test.jsx
**Location**: `frontend/src/test/TaskItem.test.jsx`

Component tests for TaskItem:

- **testRenderTaskItem()**: Verifies task details are displayed
- **testDeleteButton()**: Tests delete action on individual task
- **testToggleCompletion()**: Tests marking task as complete/incomplete
- **testCompletedStyling()**: Verifies completed tasks have proper styling

**Running the test**:
```bash
cd frontend
npm test -- TaskItem.test.jsx
```

### Setting Up Frontend Tests

First, install test dependencies:
```bash
cd frontend
npm install
```

### Running Frontend Tests

**Run all tests**:
```bash
npm test
```

**Run tests in watch mode**:
```bash
npm test -- --watch
```

**Run tests with UI**:
```bash
npm run test:ui
```

**Run specific test file**:
```bash
npm test -- App.test.jsx
```

**Run tests once (CI mode)**:
```bash
npm run test:run
```

---

## CRUD Operations Coverage

### Create (POST)
- ✅ Backend: TaskControllerTest.testCreateTask()
- ✅ Backend: TaskIntegrationTest.testCreateAndRetrieveTask()
- ✅ Backend: TaskServiceTest.testCreateTask()
- ✅ Frontend: App.test.jsx testCreateNewTask()
- ✅ Frontend: TaskForm.test.jsx testSubmitFormData()

### Read (GET)
- ✅ Backend: TaskControllerTest.testGetAllTasks()
- ✅ Backend: TaskControllerTest.testGetTaskById()
- ✅ Backend: TaskServiceTest.testGetAllTasks()
- ✅ Backend: TaskServiceTest.testGetTaskById()
- ✅ Backend: TaskIntegrationTest.testGetAllTasks()
- ✅ Frontend: App.test.jsx testFetchTasksOnMount()

### Update (PUT)
- ✅ Backend: TaskControllerTest.testUpdateTask()
- ✅ Backend: TaskServiceTest.testUpdateTaskSuccess()
- ✅ Backend: TaskServiceTest.testUpdateTaskPartial()
- ✅ Backend: TaskIntegrationTest.testCreateUpdateDeleteTask()
- ✅ Backend: TaskIntegrationTest.testCompleteTask()
- ✅ Frontend: App.test.jsx testUpdateTask()

### Delete (DELETE)
- ✅ Backend: TaskControllerTest.testDeleteTask()
- ✅ Backend: TaskServiceTest.testDeleteTaskSuccess()
- ✅ Backend: TaskIntegrationTest.testCreateUpdateDeleteTask()
- ✅ Frontend: App.test.jsx testDeleteTask()
- ✅ Frontend: TaskList.test.jsx testDeleteButton()

### Error Handling
- ✅ Not Found Responses (404)
- ✅ Server Errors (500)
- ✅ Validation Errors
- ✅ Network Failures

---

## Test Statistics

### Backend
- **Total Test Cases**: 28
- **Test Classes**: 3
- **Controller Tests**: 8
- **Service Tests**: 10
- **Integration Tests**: 10

### Frontend
- **Total Test Cases**: 25+
- **Test Files**: 4
- **Component Tests**: 14
- **Integration Tests**: 11+

---

## CI/CD Integration

### Backend Tests
To run tests as part of CI/CD pipeline:
```bash
mvn clean test
```

### Frontend Tests
To run tests as part of CI/CD pipeline:
```bash
npm install
npm run test:run
```

---

## Best Practices Implemented

1. **Isolation**: Unit tests use mocks to isolate components
2. **Coverage**: Both happy path and error scenarios are tested
3. **Naming**: Clear, descriptive test names following `test<Action><Condition>` pattern
4. **Setup/Teardown**: Proper BeforeEach setup for consistent test state
5. **Assertions**: Multiple assertions to thoroughly verify behavior
6. **Mocking**: External dependencies (API, database) are properly mocked
7. **Integration Tests**: Full workflows tested end-to-end
8. **User-Centric**: Frontend tests simulate actual user interactions

---

## Troubleshooting

### Backend Tests
If tests fail with database errors, ensure H2 is included in pom.xml.
Clear Maven cache if needed:
```bash
mvn clean
rm -rf ~/.m2/repository
mvn install
```

### Frontend Tests
If tests timeout, increase timeout in vitest.config.js:
```javascript
test: {
  testTimeout: 10000
}
```

Clear node_modules if dependencies are missing:
```bash
rm -rf node_modules
npm install
```

---

## Future Enhancements

- Add performance benchmarking tests
- Implement mutation testing for code quality
- Add visual regression testing for UI components
- Add E2E tests with Playwright or Cypress
- Increase code coverage targets
- Add security/vulnerability scanning
