# Task Management Application

A full-stack web application for managing tasks with Create, Read, Update, and Delete (CRUD) operations.

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.1
- **Language**: Java 17
- **Database**: H2 (In-memory)
- **Build Tool**: Maven
- **API**: REST API with JSON

### Frontend
- **Framework**: React 18
- **Build Tool**: Vite
- **HTTP Client**: Axios
- **Styling**: CSS3

## Project Structure

```
agentic-fundamentals/
├── backend/                          # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/taskmanagement/
│   │   │   │   ├── TaskManagementApplication.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── TaskController.java
│   │   │   │   ├── service/
│   │   │   │   │   └── TaskService.java
│   │   │   │   ├── model/
│   │   │   │   │   └── Task.java
│   │   │   │   └── repository/
│   │   │   │       └── TaskRepository.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/                         # React Frontend
│   ├── src/
│   │   ├── components/
│   │   │   ├── TaskForm.jsx
│   │   │   ├── TaskList.jsx
│   │   │   └── TaskItem.jsx
│   │   ├── App.jsx
│   │   ├── App.css
│   │   └── main.jsx
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## Features

### Backend Features
- ✅ Create new tasks
- ✅ Retrieve all tasks
- ✅ Retrieve task by ID
- ✅ Update task details
- ✅ Delete tasks
- ✅ CORS enabled for frontend communication

### Frontend Features
- ✅ Add new tasks with title and description
- ✅ View all tasks
- ✅ Mark tasks as complete/incomplete
- ✅ Edit existing tasks
- ✅ Delete tasks
- ✅ Filter tasks (All, Active, Completed)
- ✅ Display task statistics
- ✅ Responsive design
- ✅ Error handling

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}` | Update task |
| DELETE | `/api/tasks/{id}` | Delete task |

## Prerequisites

- Java 17 or higher
- Maven 3.8.1 or higher
- Node.js 18+ and npm

## Setup and Installation

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080`

**H2 Console**: Access at `http://localhost:8080/h2-console` (optional for development)

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

The frontend will be available at `http://localhost:5173`

## Usage

1. **Start the backend** first on port 8080
2. **Start the frontend** on port 5173
3. Open `http://localhost:5173` in your browser
4. Add, edit, complete, or delete tasks as needed

## API Request Examples

### Create a Task
```json
POST /api/tasks
Content-Type: application/json

{
  "title": "Buy groceries",
  "description": "Buy milk, eggs, and bread",
  "completed": false
}
```

### Update a Task
```json
PUT /api/tasks/1
Content-Type: application/json

{
  "title": "Buy groceries",
  "description": "Buy milk, eggs, bread, and butter",
  "completed": true
}
```

## Development

### Backend Technologies Used
- Spring Boot Web for REST API
- Spring Data JPA for database operations
- H2 for in-memory database
- Lombok for reducing boilerplate code

### Frontend Technologies Used
- React 18 for UI components
- Vite for fast development build tool
- Axios for HTTP requests
- CSS3 for styling

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Task categories/tags
- [ ] Due dates and reminders
- [ ] Task priority levels
- [ ] Dark mode
- [ ] Database persistence (switch from H2 to PostgreSQL)
- [ ] Unit and integration tests
- [ ] Deployment to cloud platforms

## License

This project is open-source and available for educational purposes.

## Support

For issues or questions, please open an issue in the repository.
