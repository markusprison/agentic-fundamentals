import { describe, it, expect, beforeEach, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from '../App';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';

const mock = new MockAdapter(axios);

describe('App Component - CRUD Operations', () => {
  beforeEach(() => {
    mock.reset();
    mock.onGet('http://localhost:8080/api/tasks').reply(200, []);
  });

  it('should render the App component', () => {
    render(<App />);
    expect(screen.getByText(/Task Management/i)).toBeDefined();
  });

  it('should display TaskForm and TaskList components', () => {
    render(<App />);
    expect(screen.getByPlaceholderText(/Enter task title/i)).toBeDefined();
    expect(screen.getByText(/Task Manager/i)).toBeDefined();
  });

  it('should fetch and display tasks on mount', async () => {
    const mockTasks = [
      {
        id: 1,
        title: 'Test Task 1',
        description: 'Description 1',
        completed: false,
        status: 'TODO',
      },
      {
        id: 2,
        title: 'Test Task 2',
        description: 'Description 2',
        completed: true,
        status: 'DONE',
      },
    ];

    mock.onGet('http://localhost:8080/api/tasks').reply(200, mockTasks);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Test Task 1')).toBeDefined();
      expect(screen.getByText('Test Task 2')).toBeDefined();
    });
  });

  it('should display error message when fetching tasks fails', async () => {
    mock.onGet('http://localhost:8080/api/tasks').reply(500);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText(/Failed to fetch tasks/i)).toBeDefined();
    });
  });

  it('should create a new task', async () => {
    const newTask = {
      id: 3,
      title: 'New Task',
      description: 'New Task Description',
      completed: false,
      status: 'TODO',
    };

    mock.onGet('http://localhost:8080/api/tasks').reply(200, []);
    mock.onPost('http://localhost:8080/api/tasks').reply(201, newTask);

    const user = userEvent.setup();
    render(<App />);

    const titleInput = screen.getByPlaceholderText(/Enter task title/i);
    const descriptionInput = screen.getByPlaceholderText(/Enter task description/i);
    const submitButton = screen.getByText(/Add Task/i);

    await user.type(titleInput, 'New Task');
    await user.type(descriptionInput, 'New Task Description');
    await user.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('New Task')).toBeDefined();
    });
  });

  it('should display error when task creation fails', async () => {
    mock.onGet('http://localhost:8080/api/tasks').reply(200, []);
    mock.onPost('http://localhost:8080/api/tasks').reply(500);

    const user = userEvent.setup();
    render(<App />);

    const titleInput = screen.getByPlaceholderText(/Enter task title/i);
    const submitButton = screen.getByText(/Add Task/i);

    await user.type(titleInput, 'New Task');
    await user.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText(/Failed to create task/i)).toBeDefined();
    });
  });

  it('should update a task', async () => {
    const initialTask = {
      id: 1,
      title: 'Initial Title',
      description: 'Initial Description',
      completed: false,
      status: 'TODO',
    };

    const updatedTask = {
      id: 1,
      title: 'Updated Title',
      description: 'Initial Description',
      completed: true,
      status: 'DONE',
    };

    mock.onGet('http://localhost:8080/api/tasks').reply(200, [initialTask]);
    mock.onPut('http://localhost:8080/api/tasks/1').reply(200, updatedTask);

    const user = userEvent.setup();
    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Initial Title')).toBeDefined();
    });

    const editButton = screen.getByTitle('Edit task');
    await user.click(editButton);

    await waitFor(() => {
      const titleInput = screen.getByDisplayValue('Initial Title');
      expect(titleInput).toBeDefined();
    });
  });

  it('should delete a task', async () => {
    const task = {
      id: 1,
      title: 'Task to Delete',
      description: 'This will be deleted',
      completed: false,
      status: 'TODO',
    };

    mock.onGet('http://localhost:8080/api/tasks').reply(200, [task]);
    mock.onDelete('http://localhost:8080/api/tasks/1').reply(204);

    const user = userEvent.setup();
    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Task to Delete')).toBeDefined();
    });

    // Mock window.confirm to return true
    global.confirm = vi.fn(() => true);

    const deleteButton = screen.getByTitle('Delete task');
    await user.click(deleteButton);

    await waitFor(() => {
      expect(screen.queryByText('Task to Delete')).toBeNull();
    });
  });

  it('should display error when task deletion fails', async () => {
    const task = {
      id: 1,
      title: 'Task to Delete',
      description: 'This will fail to delete',
      completed: false,
      status: 'TODO',
    };

    mock.onGet('http://localhost:8080/api/tasks').reply(200, [task]);
    mock.onDelete('http://localhost:8080/api/tasks/1').reply(500);

    const user = userEvent.setup();
    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Task to Delete')).toBeDefined();
    });

    // Mock window.confirm to return true
    global.confirm = vi.fn(() => true);

    const deleteButton = screen.getByTitle('Delete task');
    await user.click(deleteButton);

    await waitFor(() => {
      expect(screen.getByText(/Failed to delete task/i)).toBeDefined();
    }, { timeout: 3000 });
  });
});
