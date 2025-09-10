# Task Management System

This project is a Java-based task management system for organizing and tracking tasks assigned to employees. The system supports both simple tasks (with start/end hours) and complex tasks (with multiple subtasks), tracks their status, and provides workload statistics for employees. A JavaFX GUI is included for interaction, along with data persistence through serialization.

## Features

- Create and manage simple tasks with start and end hours. Create complex tasks consisting of multiple subtasks, that can be either simple or complex.
- Update and delete tasks (backend logic implemented).
- Prevents duplicate task IDs.
- Automatically updates complex task status based on the status of its subtasks.
- Assign tasks to employees and estimate duration. View employees and their assigned tasks.
- Statistics: filter employees with workload > 40 hours, count completed vs uncompleted tasks.
- Save and load system data using serialization.
- JavaFX GUI with tabs for management, viewing, modifting, and statistics.

## Technologies Used
- **Language:** Java
- **Frameworks & Tools:** JavaFX, IntelliJ IDEA
- **Concepts Used:** Object-Oriented Programming (OOP), Serialization, GUI Development

## How It Works
The system manages employees and tasks through internal mapping of employees to their assigned tasks. SimpleTask stores start and end hours and computes duration. ComplexTask groups subtasks and automatically updates its status based on them. Employees can be assigned multiple tasks, and workloads are calculated from task durations. Data persistence is handled by saving/loading serialized objects (.ser files).

## Future Improvements
- Export reports.
- Add deadlines and priority levels to tasks.
- Replace serialization with database persistence.
- Add GUI support for updating and deleting tasksyeah.
