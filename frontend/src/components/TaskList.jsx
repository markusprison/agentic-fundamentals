import React, { useState, useMemo } from 'react';
import TaskItem from './TaskItem';

function TaskList({ tasks, onToggleTask, onDeleteTask, onUpdateTask }) {
  const [filter, setFilter] = useState('all');
  const [sortBy, setSortBy] = useState('date');

  const filteredTasks = tasks.filter(task => {
    if (filter === 'completed') return task.status === 'DONE';
    if (filter === 'active') return task.status === 'TODO' || task.status === 'IN_PROGRESS';
    return true;
  });

  const sortedTasks = useMemo(() => {
    const sorted = [...filteredTasks];
    
    if (sortBy === 'date') {
      sorted.sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt));
    } else if (sortBy === 'status') {
      const statusOrder = { 'TODO': 0, 'IN_PROGRESS': 1, 'DONE': 2 };
      sorted.sort((a, b) => {
        const statusDiff = statusOrder[a.status] - statusOrder[b.status];
        if (statusDiff !== 0) return statusDiff;
        return new Date(b.updatedAt) - new Date(a.updatedAt);
      });
    } else if (sortBy === 'due') {
      sorted.sort((a, b) => {
        if (!a.dueDate && !b.dueDate) return 0;
        if (!a.dueDate) return 1;
        if (!b.dueDate) return -1;
        return new Date(a.dueDate) - new Date(b.dueDate);
      });
    }
    
    return sorted;
  }, [filteredTasks, sortBy]);

  const completedCount = tasks.filter(t => t.status === 'DONE').length;
  const activeCount = tasks.filter(t => t.status === 'TODO' || t.status === 'IN_PROGRESS').length;

  return (
    <div className="task-list-section">
      <div className="task-stats">
        <span className="stat">
          <strong>{tasks.length}</strong> Total
        </span>
        <span className="stat active">
          <strong>{activeCount}</strong> Active
        </span>
        <span className="stat completed">
          <strong>{completedCount}</strong> Completed
        </span>
      </div>

      <div className="controls-section">
        <div className="filter-buttons">
          <button 
            className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
            onClick={() => setFilter('all')}
          >
            All Tasks
          </button>
          <button 
            className={`filter-btn ${filter === 'active' ? 'active' : ''}`}
            onClick={() => setFilter('active')}
          >
            Active
          </button>
          <button 
            className={`filter-btn ${filter === 'completed' ? 'active' : ''}`}
            onClick={() => setFilter('completed')}
          >
            Completed
          </button>
        </div>

        <div className="sort-buttons">
          <label>Sort by:</label>
          <button 
            className={`sort-btn ${sortBy === 'date' ? 'active' : ''}`}
            onClick={() => setSortBy('date')}
          >
            Date
          </button>
          <button 
            className={`sort-btn ${sortBy === 'status' ? 'active' : ''}`}
            onClick={() => setSortBy('status')}
          >
            Status
          </button>
          <button 
            className={`sort-btn ${sortBy === 'due' ? 'active' : ''}`}
            onClick={() => setSortBy('due')}
          >
            Due Date
          </button>
        </div>
      </div>

      {sortedTasks.length === 0 ? (
        <div className="empty-state">
          <p>No tasks to display</p>
        </div>
      ) : (
        <ul className="task-list">
          {sortedTasks.map(task => (
            <TaskItem
              key={task.id}
              task={task}
              onToggle={onToggleTask}
              onDelete={onDeleteTask}
              onUpdate={onUpdateTask}
            />
          ))}
        </ul>
      )}
    </div>
  );
}

export default TaskList;
