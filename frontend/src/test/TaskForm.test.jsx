import { describe, it, expect, beforeEach, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import TaskForm from '../components/TaskForm';

describe('TaskForm Component', () => {
  let onAddTaskMock;

  beforeEach(() => {
    onAddTaskMock = vi.fn();
  });

  it('should render the form with all fields', () => {
    render(<TaskForm onAddTask={onAddTaskMock} />);

    expect(screen.getByLabelText(/Task Title/i)).toBeDefined();
    expect(screen.getByLabelText(/Description/i)).toBeDefined();
    expect(screen.getByLabelText(/Status/i)).toBeDefined();
    expect(screen.getByText(/Add Task/i)).toBeDefined();
  });

  it('should not submit if title is empty', async () => {
    const user = userEvent.setup();
    render(<TaskForm onAddTask={onAddTaskMock} />);

    const submitButton = screen.getByText(/Add Task/i);
    await user.click(submitButton);

    expect(onAddTaskMock).not.toHaveBeenCalled();
  });

  it('should submit form with task data', async () => {
    const user = userEvent.setup();
    render(<TaskForm onAddTask={onAddTaskMock} />);

    const titleInput = screen.getByPlaceholderText(/Enter task title/i);
    const descriptionInput = screen.getByPlaceholderText(/Enter task description/i);
    const submitButton = screen.getByText(/Add Task/i);

    await user.type(titleInput, 'Test Task');
    await user.type(descriptionInput, 'Test Description');
    await user.click(submitButton);

    expect(onAddTaskMock).toHaveBeenCalledWith(
      expect.objectContaining({
        title: 'Test Task',
        description: 'Test Description',
        status: 'TODO',
        completed: false,
      })
    );
  });

  it('should clear form after submission', async () => {
    const user = userEvent.setup();
    render(<TaskForm onAddTask={onAddTaskMock} />);

    const titleInput = screen.getByPlaceholderText(/Enter task title/i);
    const submitButton = screen.getByText(/Add Task/i);

    await user.type(titleInput, 'Test Task');
    await user.click(submitButton);

    expect(titleInput.value).toBe('');
  });

  it('should enforce title max length of 100 characters', async () => {
    const user = userEvent.setup();
    render(<TaskForm onAddTask={onAddTaskMock} />);

    const titleInput = screen.getByPlaceholderText(/Enter task title/i);
    const longTitle = 'a'.repeat(150);

    await user.type(titleInput, longTitle);

    expect(titleInput.value.length).toBeLessThanOrEqual(100);
  });

  it('should enforce description max length of 500 characters', () => {
    render(<TaskForm onAddTask={onAddTaskMock} />);

    const descriptionInput = screen.getByPlaceholderText(/Enter task description/i);
    
    // Verify the input has maxlength attribute set to 500
    expect(descriptionInput.maxLength).toBe(500);
  });

  it('should update status dropdown', async () => {
    const user = userEvent.setup();
    render(<TaskForm onAddTask={onAddTaskMock} />);

    const statusSelect = screen.getByLabelText(/Status/i);
    await user.selectOptions(statusSelect, 'IN_PROGRESS');

    expect(statusSelect.value).toBe('IN_PROGRESS');
  });
});
