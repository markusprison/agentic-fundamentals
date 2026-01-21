import React, { useState } from 'react';

function TaskItem({ task, onToggle, onDelete, onUpdate }) {
  const [isEditing, setIsEditing] = useState(false);
  const [editedTitle, setEditedTitle] = useState(task.title);
  const [editedDescription, setEditedDescription] = useState(task.description || '');
  const [editedStatus, setEditedStatus] = useState(task.status || 'TODO');
  const [editedDueDate, setEditedDueDate] = useState(
    task.dueDate ? new Date(task.dueDate).toISOString().slice(0, 16) : ''
  );

  const handleSave = () => {
    if (editedTitle.trim()) {
      onUpdate(task.id, {
        ...task,
        title: editedTitle.trim().substring(0, 100),
        description: editedDescription.trim().substring(0, 500),
        status: editedStatus,
        dueDate: editedDueDate ? new Date(editedDueDate).toISOString() : null
      });
      setIsEditing(false);
    }
  };

  const handleCancel = () => {
    setEditedTitle(task.title);
    setEditedDescription(task.description || '');
    setEditedStatus(task.status || 'TODO');
    setEditedDueDate(
      task.dueDate ? new Date(task.dueDate).toISOString().slice(0, 16) : ''
    );
    setIsEditing(false);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'TODO':
        return 'status-todo';
      case 'IN_PROGRESS':
        return 'status-in-progress';
      case 'DONE':
        return 'status-done';
      default:
        return 'status-todo';
    }
  };

  const isDueSoon = (dueDate) => {
    if (!dueDate) return false;
    const due = new Date(dueDate);
    const now = new Date();
    const daysUntilDue = (due - now) / (1000 * 60 * 60 * 24);
    return daysUntilDue <= 1 && daysUntilDue > 0;
  };

  const isOverdue = (dueDate) => {
    if (!dueDate) return false;
    return new Date(dueDate) < new Date();
  };

  const handleDelete = () => {
    const confirmed = window.confirm(
      `Are you sure you want to delete the task "${task.title}"?`
    );
    if (confirmed) {
      onDelete(task.id);
    }
  };

  if (isEditing) {
    return (
      <li className="task-item editing">
        <div className="task-edit-form">
          <input
            type="text"
            value={editedTitle}
            onChange={(e) => setEditedTitle(e.target.value.substring(0, 100))}
            maxLength="100"
            className="edit-input"
            placeholder="Task title (max 100 chars)"
          />
          <textarea
            value={editedDescription}
            onChange={(e) => setEditedDescription(e.target.value.substring(0, 500))}
            maxLength="500"
            className="edit-textarea"
            rows="2"
            placeholder="Description (max 500 chars)"
          />
          <select
            value={editedStatus}
            onChange={(e) => setEditedStatus(e.target.value)}
            className="edit-input"
          >
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="DONE">DONE</option>
          </select>
          <input
            type="datetime-local"
            value={editedDueDate}
            onChange={(e) => setEditedDueDate(e.target.value)}
            className="edit-input"
          />
          <div className="edit-buttons">
            <button onClick={handleSave} className="btn btn-success">Save</button>
            <button onClick={handleCancel} className="btn btn-cancel">Cancel</button>
          </div>
        </div>
      </li>
    );
  }

  return (
    <li className={`task-item ${task.completed ? 'completed' : ''} ${getStatusColor(task.status)}`}>
      <div className="task-content">
        <input
          type="checkbox"
          checked={task.completed}
          onChange={() => onToggle(task.id)}
          className="task-checkbox"
        />
        <div className="task-text">
          <h3>{task.title}</h3>
          {task.description && <p>{task.description}</p>}
          <div className="task-meta">
            <span className={`task-status ${getStatusColor(task.status)}`}>
              {task.status}
            </span>
            <span className="task-date">{formatDate(task.updatedAt)}</span>
            {task.dueDate && (
              <span className={`task-due-date ${isOverdue(task.dueDate) ? 'overdue' : isDueSoon(task.dueDate) ? 'due-soon' : ''}`}>
                📅 Due: {formatDate(task.dueDate)}
              </span>
            )}
          </div>
        </div>
      </div>
      <div className="task-actions">
        <button 
          onClick={() => setIsEditing(true)} 
          className="btn btn-edit"
          title="Edit task"
        >
          ✎
        </button>
        <button 
          onClick={handleDelete} 
          className="btn btn-delete"
          title="Delete task"
        >
          ✕
        </button>
      </div>
    </li>
  );
}

export default TaskItem;
