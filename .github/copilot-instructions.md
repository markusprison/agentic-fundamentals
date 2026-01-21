# .github/copilot-instructions.md - Copilot workspace instructions

This is a full-stack Task Management Application project with:
- **Backend**: Spring Boot REST API with H2 Database
- **Frontend**: React.js with Vite
- **Communication**: JSON-based REST API

## Quick Start

### Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Runs on `http://localhost:8080`

### Frontend  
```bash
cd frontend
npm install
npm run dev
```
Runs on `http://localhost:5173`

## Architecture
- Spring Boot 3.2.1 with Java 17
- React 18 with Vite
- H2 in-memory database
- CORS enabled for cross-origin requests
