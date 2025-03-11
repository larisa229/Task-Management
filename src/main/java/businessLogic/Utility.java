package businessLogic;

import dataModel.Employee;
import dataModel.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    public List<Employee> filterEmployees(TasksManagement tasksManagement) {
        List<Employee> filteredEmployees = new ArrayList<>();

        for (Employee employee : tasksManagement.getTasksToEmployees().keySet()) {
            int workDuration = tasksManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());
            if (workDuration > 40) {
                filteredEmployees.add(employee);
            }
        }

        filteredEmployees.sort((e1, e2) -> {
            int duration1 = tasksManagement.calculateEmployeeWorkDuration(e1.getIdEmployee());
            int duration2 = tasksManagement.calculateEmployeeWorkDuration(e2.getIdEmployee());
            return Integer.compare(duration1, duration2);
        });

        return filteredEmployees;
    }

    public static Map<String, Map<String, Integer>> calculateTaskStatusCounts(TasksManagement tasksManagement) {
        Map<String, Map<String, Integer>> statusCounts = new HashMap<>();

        for (Employee employee : tasksManagement.getTasksToEmployees().keySet()) {
            List<Task> tasks = tasksManagement.getTasksToEmployees().get(employee);
            Map<String, Integer> taskCount = new HashMap<>();
            taskCount.put("Completed", 0);
            taskCount.put("Uncompleted", 0);

            for (Task task : tasks) {
                if (task.getStatusTask().equals("Completed")) {
                    taskCount.put("Completed", taskCount.get("Completed") + 1);
                } else {
                    taskCount.put("Uncompleted", taskCount.get("Uncompleted") + 1);
                }
            }

            statusCounts.put(employee.getName(), taskCount);
        }
        return statusCounts;
    }

}
