import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import TaskList from '../components/TaskList';

describe('TaskList Component', () => {
  it('should render task list with tasks', () => {
    const tasks = [
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

    const onUpdateTaskMock = vi.fn();
    const onDeleteTaskMock = vi.fn();
    const onToggleMock = vi.fn();

    render(
      <TaskList
        tasks={tasks}
        onUpdateTask={onUpdateTaskMock}
        onDeleteTask={onDeleteTaskMock}
        onToggleTask={onToggleMock}
      />
    );

    expect(screen.getByText('Test Task 1')).toBeDefined();
    expect(screen.getByText('Test Task 2')).toBeDefined();
  });

  it('should render empty message when no tasks', () => {
    const onUpdateTaskMock = vi.fn();
    const onDeleteTaskMock = vi.fn();
    const onToggleMock = vi.fn();

    render(
      <TaskList
        tasks={[]}
        onUpdateTask={onUpdateTaskMock}
        onDeleteTask={onDeleteTaskMock}
        onToggleTask={onToggleMock}
      />
    );

    expect(screen.getByText(/No tasks to display/i)).toBeDefined();
  });

  it('should call onDeleteTask when delete button is clicked', async () => {
    const user = userEvent.setup();
    const tasks = [
      {
        id: 1,
        title: 'Test Task',
        description: 'Description',
        completed: false,
        status: 'TODO',
      },
    ];

    const onUpdateTaskMock = vi.fn();
    const onDeleteTaskMock = vi.fn();
    const onToggleMock = vi.fn();

    render(
      <TaskList
        tasks={tasks}
        onUpdateTask={onUpdateTaskMock}
        onDeleteTask={onDeleteTaskMock}
        onToggleTask={onToggleMock}
      />
    );

    // Mock window.confirm to return true
    global.confirm = vi.fn(() => true);

    const deleteButton = screen.getByTitle('Delete task');
    await user.click(deleteButton);

    expect(onDeleteTaskMock).toHaveBeenCalledWith(1);
  });

  it('should display completed status', () => {
    const tasks = [
      {
        id: 1,
        title: 'Completed Task',
        description: 'This is completed',
        completed: true,
        status: 'DONE',
      },
    ];

    const onUpdateTaskMock = vi.fn();
    const onDeleteTaskMock = vi.fn();
    const onToggleMock = vi.fn();

    render(
      <TaskList
        tasks={tasks}
        onUpdateTask={onUpdateTaskMock}
        onDeleteTask={onDeleteTaskMock}
        onToggleTask={onToggleMock}
      />
    );

    const taskItem = screen.getByText('Completed Task');
    expect(taskItem).toBeDefined();
  });
});
