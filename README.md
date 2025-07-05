# Task Management System

A simple Java-based task management system that allows organizing tasks (simple and complex) and assigning them to employees. The system supports tracking task statuses, estimating task durations, and persisting data.

## Features

- **Task Management**: Create, update, and delete tasks.
  - Supports **SimpleTask** (with start and end hours).
  - Supports **ComplexTask** consisting of multiple subtasks.
- **Employee Management**: Add employees and assign tasks to them.
- **Task Assignment**: Assign tasks to employees and track work durations.
- **Status Tracking**: Tasks can be marked as "Completed" or "Uncompleted".
- **Task Duration Estimation**: Calculate duration of tasks based on hours or subtasks.
- **Data Persistence**: Save and load task management data, employees, and tasks from files.
- **Filtering & Reporting**:
  - Filter employees based on workload.
  - Calculate task status counts per employee.

## Usage
- Create employees and tasks.
- Assign tasks to employees using TasksManagement.assignTaskToEmployee.
- Modify task status with TasksManagement.modifyTaskStatus.
- Calculate work durations and generate reports via utility methods.
- Save/load data using DataPersistence.
