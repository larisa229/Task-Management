package businessLogic;

import dataModel.ComplexTask;
import dataModel.Employee;
import dataModel.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksManagement implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<Employee, List<Task>> tasksToEmployees;

    public TasksManagement() {
        this.tasksToEmployees = new HashMap<>();
    }

    public Map<Employee, List<Task>> getTasksToEmployees() {
        return tasksToEmployees;
    }

    public Employee getEmployeeById(int idEmployee) {
        for (Employee employee : tasksToEmployees.keySet()) {
            if (employee.getIdEmployee() == idEmployee) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : tasksToEmployees.keySet()) {
            employees.add(employee);
        }
        return employees;
    }

    public void assignTaskToEmployee(int idEmployee, Task task) {

        Employee employee = getEmployeeById(idEmployee);

        if (employee == null) {
            System.out.println("Employee " + idEmployee + " does not exist");
            return;
        }

        tasksToEmployees.putIfAbsent(employee, new ArrayList<>());
        List<Task> taskList = tasksToEmployees.get(employee);
        for (Task t : taskList) {
            if (t.getIdTask() == task.getIdTask()) {
                throw new IllegalArgumentException("Task " + t.getIdTask() + " already assigned to employee " + idEmployee);
            }
        }
        taskList.add(task);
    }

    public int calculateEmployeeWorkDuration(int idEmployee) {
        Employee employee = getEmployeeById(idEmployee);
        if (employee == null) {
            System.out.println("Employee " + idEmployee + " does not exist");
            return 0;
        }
        int employeeWorkDuration = 0;
        for(Task t : tasksToEmployees.get(employee)) {
            employeeWorkDuration += t.estimateDuration();
        }
        return employeeWorkDuration;
    }

    public void modifyTaskStatus(int idEmployee, int idTask) {
        Employee employee = getEmployeeById(idEmployee);
        if (employee == null) {
            System.out.println("Employee " + idEmployee + " does not exist");
            return;
        }
        for(Task t : tasksToEmployees.get(employee)) {
            if(t.getIdTask() == idTask) {
                if(t.getStatusTask().equals("Completed")) {
                    if(t instanceof ComplexTask) {
                        for(Task task : ((ComplexTask) t).getSubtasks()) {
                            task.setStatusTask("Uncompleted");
                        }
                        ((ComplexTask) t).updateStatus();
                    }
                    t.setStatusTask("Uncompleted");
                } else {
                    if(t instanceof ComplexTask) {
                        for(Task task : ((ComplexTask) t).getSubtasks()) {
                            task.setStatusTask("Completed");
                        }
                        ((ComplexTask) t).updateStatus();
                    }
                    t.setStatusTask("Completed");
                }
                break;
            }
        }
    }

    public List<Task> getTasks() {
        List<Task> uniqueTasks = new ArrayList<>();
        // track task IDs to avoid duplicates
        List<Integer> addedTaskIds = new ArrayList<>();

        for (List<Task> tasks : tasksToEmployees.values()) {
            for (Task task : tasks) {
                if (!addedTaskIds.contains(task.getIdTask())) {
                    uniqueTasks.add(task);
                    addedTaskIds.add(task.getIdTask());
                }
            }
        }
        return uniqueTasks;
    }

    public Task getTaskById(int id) {
        for (List<Task> tasks : tasksToEmployees.values()) {
            for (Task task : tasks) {
                if (task.getIdTask() == id) {
                    return task;
                }
            }
        }
        return null;
    }
}
