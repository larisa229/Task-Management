package com.example.pt2025_30422_pasca_larisa_assignment_1;

import businessLogic.TasksManagement;
import businessLogic.Utility;
import dataAccess.DataPersistence;
import dataModel.ComplexTask;
import dataModel.Employee;
import dataModel.SimpleTask;
import dataModel.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Employee emp1 = new Employee(1, "Alice");
        Employee emp2 = new Employee(2, "Bob");
        Employee emp3 = new Employee(3, "Marta");

        SimpleTask task1 = new SimpleTask(101, "Completed", 8, 22);
        SimpleTask task2 = new SimpleTask(102, "Completed", 15, 17);
        SimpleTask task3 = new SimpleTask(103, "Completed", 10, 19);

        ComplexTask complexTask = new ComplexTask(201);
        complexTask.addTask(task1);
        complexTask.addTask(task2);

        TasksManagement tasksManagement = new TasksManagement();

        tasksManagement.getTasksToEmployees().put(emp1, new ArrayList<>());
        tasksManagement.getTasksToEmployees().put(emp2, new ArrayList<>());

        tasksManagement.assignTaskToEmployee(1, task1);
        tasksManagement.assignTaskToEmployee(1, task2);
        tasksManagement.assignTaskToEmployee(2, task3);
        tasksManagement.assignTaskToEmployee(2, complexTask);

        DataPersistence dataPersistence = new DataPersistence(tasksManagement);

        dataPersistence.saveTaskManagement("task_management.ser");
        dataPersistence.saveEmployees(tasksManagement, "employees.ser");
        dataPersistence.saveTasks(tasksManagement, "tasks.ser");

        TasksManagement tasksManagement2 = new TasksManagement();

        tasksManagement2 = DataPersistence.loadTaskManagement("task_management.ser");
        List<Employee> employees = DataPersistence.loadEmployees("employees.ser");
        List<Task> tasks = DataPersistence.loadTasks("tasks.ser");

        tasksManagement2.getTasksToEmployees().put(emp3, new ArrayList<>());

        tasksManagement2.assignTaskToEmployee(3, task3);
        tasksManagement2.assignTaskToEmployee(3, task1);

        System.out.println("Employee work durations:");
        System.out.println(emp1.getName() + ": " + tasksManagement2.calculateEmployeeWorkDuration(1) + " hours");
        System.out.println(emp2.getName() + ": " + tasksManagement2.calculateEmployeeWorkDuration(2) + " hours");
        System.out.println(emp3.getName() + ": " + tasksManagement2.calculateEmployeeWorkDuration(3) + " hours");

        Utility utility = new Utility();
        utility.filterEmployees(tasksManagement2);

        System.out.println("\nModifying task status...");
        tasksManagement.modifyTaskStatus(1, 102);

        System.out.println("\nUpdated Employee work durations:");
        System.out.println(emp1.getName() + ": " + tasksManagement2.calculateEmployeeWorkDuration(1) + " hours");
        System.out.println(emp2.getName() + ": " + tasksManagement2.calculateEmployeeWorkDuration(2) + " hours");
        System.out.println(emp3.getName() + ": " + tasksManagement2.calculateEmployeeWorkDuration(3) + " hours");
        
        System.out.println("\nTask Status Counts:");
        Map<String, Map<String, Integer>> statusCounts = Utility.calculateTaskStatusCounts(tasksManagement);
        for (String employee : statusCounts.keySet()) {
            System.out.println(employee + " -> " + statusCounts.get(employee));
        }
    }
}
