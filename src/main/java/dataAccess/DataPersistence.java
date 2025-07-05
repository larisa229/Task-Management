package dataAccess;

import businessLogic.TasksManagement;
import dataModel.Employee;
import dataModel.Task;

import java.io.*;
import java.util.List;

public class DataPersistence implements Serializable {

    private static final long serialVersionUID = 1L;
    private TasksManagement tasksManagement;

    public DataPersistence(TasksManagement tasksManagement) {
        this.tasksManagement = tasksManagement;
    }

    public void saveTaskManagement(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tasksManagement);
            System.out.println("Data from Tasks Management saved successfully to  " + filename);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    //static because we want to call this method only using the class, this way it doesn't belong to an instance, but to the class
    public static TasksManagement loadTaskManagement(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (TasksManagement) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return new TasksManagement();
        }
    }

    public void saveEmployees(TasksManagement tasksManagement, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tasksManagement.getEmployees());
            System.out.println("Employee data saved successfully to  " + filename);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public static List<Employee> loadEmployees(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Employee> employees = (List<Employee>) ois.readObject();
            return employees;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
        return null;
    }

    public void saveTasks(TasksManagement tasksManagement, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(tasksManagement.getTasks());
            System.out.println("Tasks data saved successfully to  " + filename);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public static List<Task> loadTasks(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Task> tasks = (List<Task>) ois.readObject();
            return tasks;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
        return null;
    }
}
