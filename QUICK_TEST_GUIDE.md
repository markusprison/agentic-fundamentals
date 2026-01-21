# Quick Test Execution Guide

## Backend Tests (Java/Spring Boot)

### Build Backend (Required first time)
```bash
cd backend
mvn clean install
```

### Run All Backend Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
# Controller Tests
mvn test -Dtest=TaskControllerTest

# Service Tests
mvn test -Dtest=TaskServiceTest

# Integration Tests
mvn test -Dtest=TaskIntegrationTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=TaskControllerTest#testCreateTask
```

### Run with Coverage Report
```bash
mvn clean test jacoco:report
# Report generated in: target/site/jacoco/index.html
```

---

## Frontend Tests (React/Vitest)

### Install Dependencies (First Time)
```bash
cd frontend
npm install
```

### Run All Tests
```bash
npm test
```

### Run Tests in Watch Mode
```bash
npm test -- --watch
```

### Run Tests with UI Dashboard
```bash
npm run test:ui
# Opens browser at http://localhost:51204
```

### Run Specific Test File
```bash
npm test -- App.test.jsx
npm test -- TaskForm.test.jsx
npm test -- TaskList.test.jsx
npm test -- TaskItem.test.jsx
```

### Run Tests Once (CI Mode)
```bash
npm run test:run
```

### Run Tests with Coverage
```bash
npm test -- --coverage
```

---

## Complete CRUD Test Scenarios

### Backend - Full CRUD Flow
```bash
# All CRUD tests included in TaskIntegrationTest
mvn test -Dtest=TaskIntegrationTest

# Tests:
# - Create task
# - Read task
# - Update task (including mark as complete)
# - Delete task
# - Multiple task operations
```

### Frontend - Full CRUD Flow
```bash
npm test -- App.test.jsx

# Tests:
# - Create task via form
# - Display all tasks
# - Edit task
# - Delete task
# - Error handling
```

---

## Validation Coverage

✅ Task Creation
- Title validation (required, max 100 chars)
- Description validation (max 500 chars)
- Status selection
- Due date handling

✅ Task Retrieval
- Get all tasks
- Get single task by ID
- Handle not found (404)

✅ Task Updates
- Full update
- Partial update
- Status changes
- Completion toggling

✅ Task Deletion
- Delete single task
- Verify deletion
- Handle not found (404)

✅ Error Handling
- Server errors (500)
- Validation errors
- Network failures
- Not found errors (404)

---

## Sample Test Output

### Successful Backend Test Run
```
[INFO] Running com.taskmanagement.controller.TaskControllerTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.345 s
[INFO] Running com.taskmanagement.service.TaskServiceTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.234 s
[INFO] Running com.taskmanagement.integration.TaskIntegrationTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.456 s
[INFO] BUILD SUCCESS
```

### Successful Frontend Test Run
```
✓ App Component - CRUD Operations (15 tests)
  ✓ should render the App component
  ✓ should create a new task
  ✓ should update a task
  ✓ should delete a task
  ... more tests ...

✓ TaskForm Component (7 tests)
✓ TaskList Component (4 tests)
✓ TaskItem Component (4 tests)

Test Files  4 passed (4)
     Tests  34 passed (34)
```

---

## Continuous Integration Commands

### Complete CI/CD Pipeline
```bash
# Backend
cd backend
mvn clean verify

# Frontend
cd frontend
npm install
npm run test:run

# Both pass = Ready to deploy!
```

---

## Troubleshooting Tests

### Backend Tests Fail
```bash
# Clear Maven cache
mvn clean
rm -rf ~/.m2/repository

# Rebuild
mvn clean install
mvn test
```

### Frontend Tests Fail
```bash
# Clear dependencies
rm -rf node_modules
npm install

# Clear Vitest cache
rm -rf .vitest

# Run tests again
npm test
```

### Port Already in Use
```bash
# Kill process on port 8080 (backend)
lsof -ti:8080 | xargs kill -9

# Kill process on port 5173 (frontend)
lsof -ti:5173 | xargs kill -9
```

---

## Test Documentation
See [TEST_DOCUMENTATION.md](TEST_DOCUMENTATION.md) for comprehensive test details.
