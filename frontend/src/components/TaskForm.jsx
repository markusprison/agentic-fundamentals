import React, { useState } from 'react';
import PropTypes from 'prop-types';

function TaskForm({ onAddTask }) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState('TODO');
  const [dueDate, setDueDate] = useState('');
  const [titleError, setTitleError] = useState('');

  const validateTitle = (value) => {
    if (!value || !value.trim()) {
      setTitleError('Title cannot be empty');
      return false;
    }
    setTitleError('');
    return true;
  };

  const handleTitleChange = (e) => {
    const value = e.target.value.substring(0, 100);
    setTitle(value);
    if (value.trim()) {
      setTitleError('');
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!validateTitle(title)) {
      return;
    }

    onAddTask({
      title: title.trim().substring(0, 100),
      description: description.trim().substring(0, 500),
      status: status,
      dueDate: dueDate ? new Date(dueDate).toISOString() : null,
      completed: false
    });
    setTitle('');
    setDescription('');
    setStatus('TODO');
    setDueDate('');
    setTitleError('');
  };

  return (
    <form className="task-form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label htmlFor="title">Task Title * ({title.length}/100)</label>
        <input
          type="text"
          id="title"
          placeholder="Enter task title"
          value={title}
          onChange={handleTitleChange}
          onBlur={() => validateTitle(title)}
          maxLength="100"
          required
          style={titleError ? { borderColor: '#d32f2f' } : {}}
        />
        {titleError && <span className="error-message" style={{ color: '#d32f2f', fontSize: '12px', marginTop: '4px' }}>{titleError}</span>}
      </div>
      <div className="form-group">
        <label htmlFor="description">Description ({description.length}/500)</label>
        <textarea
          id="description"
          placeholder="Enter task description"
          value={description}
          onChange={(e) => setDescription(e.target.value.substring(0, 500))}
          maxLength="500"
          rows="3"
        />
      </div>
      <div className="form-row">
        <div className="form-group">
          <label htmlFor="status">Status *</label>
          <select
            id="status"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
          >
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="DONE">DONE</option>
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="dueDate">Due Date</label>
          <input
            type="datetime-local"
            id="dueDate"
            value={dueDate}
            onChange={(e) => setDueDate(e.target.value)}
          />
        </div>
      </div>
      <button type="submit" className="btn btn-primary" disabled={!title.trim()}>Add Task</button>
    </form>
  );
}

TaskForm.propTypes = {
  onAddTask: PropTypes.func.isRequired
};

export default TaskForm;
