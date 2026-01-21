import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';

function App() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const API_BASE_URL = 'http://localhost:8080/api/tasks';

  // Fetch all tasks
  const fetchTasks = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get(API_BASE_URL);
      setTasks(response.data);
    } catch (err) {
      setError('Failed to fetch tasks');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Fetch tasks on component mount
  useEffect(() => {
    fetchTasks();
  }, []);

  // Add new task
  const handleAddTask = async (taskData) => {
    try {
      const response = await axios.post(API_BASE_URL, taskData);
      setTasks([...tasks, response.data]);
      setError(null);
    } catch (err) {
      setError('Failed to create task');
      console.error(err);
    }
  };

  // Update task
  const handleUpdateTask = async (id, taskData) => {
    try {
      const response = await axios.put(`${API_BASE_URL}/${id}`, taskData);
      setTasks(tasks.map(task => (task.id === id ? response.data : task)));
      setError(null);
    } catch (err) {
      setError('Failed to update task');
      console.error(err);
    }
  };

  // Delete task
  const handleDeleteTask = async (id) => {
    try {
      await axios.delete(`${API_BASE_URL}/${id}`);
      setTasks(tasks.filter(task => task.id !== id));
      setError(null);
    } catch (err) {
      setError('Failed to delete task');
      console.error(err);
    }
  };

  // Toggle task completion
  const handleToggleTask = (id) => {
    const task = tasks.find(t => t.id === id);
    if (task) {
      const newStatus = task.status === 'DONE' ? 'TODO' : 'DONE';
      handleUpdateTask(id, { ...task, status: newStatus });
    }
  };

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>📋 Task Manager</h1>
        <p>Keep track of your daily tasks</p>
      </header>

      <main className="app-main">
        {error && <div className="error-message">{error}</div>}

        <TaskForm onAddTask={handleAddTask} />

        {loading ? (
          <div className="loading">Loading tasks...</div>
        ) : (
          <TaskList 
            tasks={tasks}
            onToggleTask={handleToggleTask}
            onDeleteTask={handleDeleteTask}
            onUpdateTask={handleUpdateTask}
          />
        )}
      </main>

      <footer className="app-footer">
        <p>© 2024 Task Management Application. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default App;
