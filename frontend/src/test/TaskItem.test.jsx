import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import TaskItem from '../components/TaskItem';

describe('TaskItem Component', () => {
  it('should render task item with all details', () => {
    const task = {
      id: 1,
      title: 'Test Task',
      description: 'Test Description',
      completed: false,
      status: 'TODO',
    };

    const onUpdateTaskMock = vi.fn();
    const onDeleteTaskMock = vi.fn();
    const onToggleMock = vi.fn();

    render(
      <TaskItem
        task={task}
        onToggle={onToggleMock}
        onUpdate={onUpdateTaskMock}
        onDelete={onDeleteTaskMock}
      />
    );

    expect(screen.getByText('Test Task')).toBeDefined();
    expect(screen.getByText('Test Description')).toBeDefined();
    expect(screen.getByText('TODO')).toBeDefined();
  });

  it('should call onDeleteTask when delete button is clicked', async () => {
    const user = userEvent.setup();
    const task = {
      id: 1,
      title: 'Test Task',
      description: 'Description',
      completed: false,
      status: 'TODO',
    };

    const onUpdateTaskMock = vi.fn();
    const onDeleteTaskMock = vi.fn();
    const onToggleMock = vi.fn();

    render(
      <TaskItem
        task={task}
        onToggle={onToggleMock}
        onUpdate={onUpdateTaskMock}
        onDelete={onDeleteTaskMock}
      />
    );

    // Mock window.confirm to return true
    global.confirm = vi.fn(() => true);

    const deleteButton = screen.getByTitle('Delete task');
    await user.click(deleteButton);

    expect(onDeleteTaskMock).toHaveBeenCalledWith(1);
  });

  it('should toggle task completion status', async () => {
    const user = userEvent.setup();
    const task = {
      id: 1,
      title: 'Test Task',
      description: 'Description',
      completed: false,
      status: 'TODO',
    };

    const onUpdateTaskMock = vi.fn();
    const onDeleteTaskMock = vi.fn();
    const onToggleMock = vi.fn();

    render(
      <TaskItem
        task={task}
        onToggle={onToggleMock}
        onUpdate={onUpdateTaskMock}
        onDelete={onDeleteTaskMock}
      />
    );

    const checkbox = screen.getByRole('checkbox');
    await user.click(checkbox);

    expect(onToggleMock).toHaveBeenCalledWith(1);
  });

  it('should show completed styling for completed tasks', () => {
    const task = {
      id: 1,
      title: 'Completed Task',
      description: 'This is done',
      completed: true,
      status: 'DONE',
    };

    const onUpdateTaskMock = vi.fn();
    const onDeleteTaskMock = vi.fn();

    const { container } = render(
      <TaskItem
        task={task}
        onUpdateTask={onUpdateTaskMock}
        onDeleteTask={onDeleteTaskMock}
      />
    );

    const taskItem = container.querySelector('.task-item.completed');
    expect(taskItem).toBeDefined();
  });
});
