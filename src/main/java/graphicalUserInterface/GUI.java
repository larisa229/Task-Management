package graphicalUserInterface;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import businessLogic.TasksManagement;
import businessLogic.Utility;
import dataModel.Employee;
import dataModel.SimpleTask;
import dataModel.ComplexTask;
import dataModel.Task;
import dataAccess.DataPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUI extends Application {

    private TasksManagement tasksManagement = new TasksManagement();
    private Utility utility = new Utility();
    private DataPersistence dataPersistence = new DataPersistence(tasksManagement);

    // for automatic updates when items are added or changed
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    private ListView<String> viewTaskListView = new ListView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        MenuBar menuBar = createMenuBar();  // create a menu with the options to save or load data
        TabPane tabPane = new TabPane(); // the main layout holds several tabs

        // first tab: management tab (add employee, add task, assign task to employee options)
        Tab managementTab = new Tab("Management");
        managementTab.setContent(getManagementPane()); // displays the content of the tab
        managementTab.setClosable(false); // prevents the tab from being closed by the user
        managementTab.setStyle("-fx-background-color: #9c9ccf;");

        // second tab: view employees and the tasks assigned to them
        Tab viewTab = new Tab("View");
        viewTab.setContent(getViewPane());
        viewTab.setClosable(false);
        viewTab.setStyle("-fx-background-color: #9c9ccf;");

        // third tab: modify the status of a task
        Tab modifyTab = new Tab("Modify Task Status");
        modifyTab.setContent(getModifyTaskStatusPane());
        modifyTab.setClosable(false);
        modifyTab.setStyle("-fx-background-color: #9c9ccf;");

        // fourth tab: see the statistics provided by the Utility class
        Tab statsTab = new Tab("Statistics");
        statsTab.setContent(getStatisticsPane());
        statsTab.setClosable(false);
        statsTab.setStyle("-fx-background-color: #9c9ccf;");

        tabPane.getTabs().addAll(managementTab, viewTab, modifyTab, statsTab); // add all tabs to the tab pane

        // place the menu at the top and the tabs in the center
        BorderPane panel = new BorderPane();
        panel.setTop(menuBar);
        panel.setCenter(tabPane);

        // create the scene with the given layout, add it to the stage and display the window
        Scene scene = new Scene(panel, 800, 600);
        primaryStage.setTitle("Task Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // create the menu bar with File > Save and Load options
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        menuBar.setStyle("-fx-background-color: #8484d1;");

        // save all data by using the DataPersistence class methods
        MenuItem saveItem = new MenuItem("Save Data");
        saveItem.setOnAction(e -> {
            dataPersistence.saveTaskManagement("tasksManagement.ser");
            dataPersistence.saveEmployees(tasksManagement, "employees.ser");
            dataPersistence.saveTasks(tasksManagement, "tasks.ser");
            showAlert("Success", "Data saved successfully.");
        });

        // load all data by using the DataPersistence class methods
        MenuItem loadItem = new MenuItem("Load Data");
        loadItem.setOnAction(e -> {
            tasksManagement = DataPersistence.loadTaskManagement("tasksManagement.ser");
            dataPersistence = new DataPersistence(tasksManagement); // update DataPersistence instance
            List<Employee> employees = DataPersistence.loadEmployees("employees.ser");

            employeeList.clear();
            taskList.clear();

            // update observable lists based on loaded data
            employeeList.setAll(tasksManagement.getEmployees());
            taskList.setAll(tasksManagement.getTasks());

            for (Employee emp : employeeList) {
                if (!tasksManagement.getTasksToEmployees().containsKey(emp)) {
                    tasksManagement.getTasksToEmployees().put(emp, new ArrayList<>()); // Prevent null mappings
                }
            }
            showAlert("Success", "Data loaded successfully.");
        });

        fileMenu.getItems().addAll(saveItem, loadItem); // add the save and load options to the file menu
        menuBar.getMenus().add(fileMenu); // add the file menu to the menu bar
        return menuBar;
    }

    private Pane getManagementPane() {
        VBox mainBox = new VBox(10); // vertically arranges the components with 10-pixel spacing
        mainBox.setPadding(new Insets(10));
        mainBox.setStyle("-fx-background-color: #b6b6ef;");

        // add employee section
        Label empLabel = new Label("Add Employee");
        TextField empIdField = new TextField();
        empIdField.setPromptText("Employee ID");
        empIdField.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        TextField empNameField = new TextField();
        empNameField.setPromptText("Employee Name");
        empNameField.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        Button addEmpButton = new Button("Add Employee");
        addEmpButton.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");

        addEmpButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(empIdField.getText()); // get the employee ID as an integer
                String name = empNameField.getText(); // get the employee name
                Employee emp = new Employee(id, name);
                // check if the employee was already added
                if (tasksManagement.getEmployeeById(id) != null) {
                    showAlert("Error", "Employee with that ID already exists.");
                } else {
                    tasksManagement.getTasksToEmployees().put(emp, new ArrayList<>());
                    employeeList.add(emp);
                    showAlert("Success", "Employee added successfully.");
                    // clear the ID and name fields after adding employee
                    empIdField.clear();
                    empNameField.clear();
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid numeric Employee ID.");
            }
        });

        VBox addEmpBox = new VBox(5, empLabel, empIdField, empNameField, addEmpButton);
        addEmpBox.setPadding(new Insets(10));
        addEmpBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        // add task section
        Label taskLabel = new Label("Add Task");
        ToggleGroup taskTypeGroup = new ToggleGroup(); // ensure only one type of task can be selected at once
        RadioButton simpleTaskRB = new RadioButton("Simple Task");
        simpleTaskRB.setToggleGroup(taskTypeGroup);
        simpleTaskRB.setSelected(true); // select simple task by default
        RadioButton complexTaskRB = new RadioButton("Complex Task");
        complexTaskRB.setToggleGroup(taskTypeGroup);

        // the simple task fields
        VBox simpleTaskBox = new VBox(5);
        TextField taskIdField = new TextField();
        taskIdField.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        taskIdField.setPromptText("Task ID");
        TextField startHourField = new TextField();
        startHourField.setPromptText("Start Hour (0-24)");
        startHourField.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        TextField endHourField = new TextField();
        endHourField.setPromptText("End Hour (0-24)");
        endHourField.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        simpleTaskBox.getChildren().addAll(taskIdField, startHourField, endHourField);

        // the complex task fields
        VBox complexTaskBox = new VBox(5);
        TextField complexTaskIdField = new TextField();
        complexTaskIdField.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        complexTaskIdField.setPromptText("Task ID");
        Label subTasksLabel = new Label("Select Subtasks:");
        ListView<Task> availableTasksListView = new ListView<>(taskList);
        availableTasksListView.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        availableTasksListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        availableTasksListView.setPrefHeight(150);
        complexTaskBox.getChildren().addAll(complexTaskIdField, subTasksLabel, availableTasksListView);
        complexTaskBox.setVisible(false);

        // toggle visibility based on selected radio button
        taskTypeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (simpleTaskRB.isSelected()) {
                simpleTaskBox.setVisible(true);
                complexTaskBox.setVisible(false);
            } else if (complexTaskRB.isSelected()) {
                simpleTaskBox.setVisible(false);
                complexTaskBox.setVisible(true);
            }
        });

        Button addTaskButton = new Button("Add Task");
        addTaskButton.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");
        addTaskButton.setOnAction(e -> {
            try {
                if (simpleTaskRB.isSelected()) {
                    int taskId = Integer.parseInt(taskIdField.getText());
                    int startHour = Integer.parseInt(startHourField.getText());
                    int endHour = Integer.parseInt(endHourField.getText());
                    if(tasksManagement.getTaskById(taskId) != null) {
                        showAlert("Error", "Task with that ID already exists.");
                        return;
                    }
                    SimpleTask simpleTask = new SimpleTask(taskId, "Uncompleted", startHour, endHour);
                    taskList.add(simpleTask);
                    showAlert("Success", "Simple task added successfully.");
                    taskIdField.clear();
                    startHourField.clear();
                    endHourField.clear();

                } else if (complexTaskRB.isSelected()) {
                    int taskId = Integer.parseInt(complexTaskIdField.getText());
                    if (tasksManagement.getTaskById(taskId) != null) {
                        showAlert("Error", "Task with that ID already exists.");
                        return;
                    }
                    ComplexTask ct = new ComplexTask(taskId);
                    ObservableList<Task> selectedSubtasks = availableTasksListView.getSelectionModel().getSelectedItems();
                    // add each selected subtask to the complex task
                    for (Task t : selectedSubtasks) {
                        ct.addTask(t);
                    }
                    taskList.add(ct);
                    showAlert("Success", "Complex task added with " + selectedSubtasks.size() + " subtasks.");
                    complexTaskIdField.clear();
                    availableTasksListView.getSelectionModel().clearSelection();
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numeric values for task and hours.");
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        HBox taskTypeBox = new HBox(10, simpleTaskRB, complexTaskRB); // holds the radio buttons side by side
        VBox addTaskBox = new VBox(5, taskLabel, taskTypeBox, simpleTaskBox, complexTaskBox, addTaskButton); // arranges components vertically
        addTaskBox.setPadding(new Insets(10));
        addTaskBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        // assigning tasks to employees section
        Label assignLabel = new Label("Assign Task to Employee");
        ComboBox<Employee> employeeAssign = new ComboBox<>(employeeList);
        employeeAssign.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");
        employeeAssign.setPromptText("Select Employee");

        ComboBox<Task> taskAssign = new ComboBox<>(taskList);
        taskAssign.setPromptText("Select Task");
        taskAssign.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");

        Button assignButton = new Button("Assign Task");
        assignButton.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");
        assignButton.setOnAction(e -> {
            try {
                Employee selectedEmp = employeeAssign.getSelectionModel().getSelectedItem();
                Task selectedTask = taskAssign.getSelectionModel().getSelectedItem();
                if (selectedEmp == null || selectedTask == null) {
                    showAlert("Error", "Please select both an employee and a task.");
                    return;
                }
                tasksManagement.assignTaskToEmployee(selectedEmp.getIdEmployee(), selectedTask);
                showAlert("Success", "Task assigned to employee.");
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        VBox assignBox = new VBox(5, assignLabel, employeeAssign, taskAssign, assignButton);
        assignBox.setPadding(new Insets(10));
        assignBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        mainBox.getChildren().addAll(addEmpBox, addTaskBox, assignBox); // add all the boxes to the main box in the first tab
        return mainBox;
    }

    // display all tasks for an employee
    private Pane getViewPane() {
        HBox mainBox = new HBox(20); // places 2 lists side by side
        mainBox.setPadding(new Insets(10));
        mainBox.setStyle("-fx-background-color: #b6b6ef;");

        // show the list of all employees
        ListView<Employee> empListView = new ListView<>(employeeList);
        empListView.setPrefWidth(300);
        empListView.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        empListView.setPlaceholder(new Label("No employees added")); // if the list is empty

        viewTaskListView.setPrefWidth(500);
        viewTaskListView.setPlaceholder(new Label("Select an employee to view tasks"));
        viewTaskListView.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");

        // when an employee is selected, refresh the task list display
        empListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            refreshTaskList(newSelection);
        });

        mainBox.getChildren().addAll(empListView, viewTaskListView);
        return mainBox;
    }

    // refresh the task list display for a given employee
    private void refreshTaskList(Employee employee) {
        viewTaskListView.getItems().clear(); // clear the current list
        if (employee != null) {
            List<Task> tasks = tasksManagement.getTasksToEmployees().get(employee);
            if (tasks != null) {
                for (Task t : tasks) { // populate the list with the wanted data
                    String display = "Task ID: " + t.getIdTask() +
                            ", Status: " + t.getStatusTask() +
                            ", Est. Duration: " + t.estimateDuration() + " hours";
                    viewTaskListView.getItems().add(display);
                }
            }
        }
    }

    private Pane getModifyTaskStatusPane() {
        VBox mainBox = new VBox(10); // vertically arranges the controls
        mainBox.setPadding(new Insets(10));
        mainBox.setStyle("-fx-background-color: #b6b6ef;");

        Label modifyLabel = new Label("Modify Task Status for an Employee");
        ComboBox<Employee> empCombo = new ComboBox<>(employeeList);
        empCombo.setPromptText("Select Employee");
        empCombo.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");
        ComboBox<Task> taskCombo = new ComboBox<>();
        taskCombo.setPromptText("Select Task");
        taskCombo.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");

        // when an employee is selected, update the task combo box with the selected employee's tasks
        empCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            taskCombo.getItems().clear();
            if (newVal != null) {
                List<Task> tasks = tasksManagement.getTasksToEmployees().get(newVal);
                if (tasks != null) {
                    taskCombo.getItems().addAll(tasks);
                }
            }
        });

        Button modifyButton = new Button("Modify Task Status");
        modifyButton.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");
        modifyButton.setOnAction(e -> {
            Employee emp = empCombo.getSelectionModel().getSelectedItem();
            Task task = taskCombo.getSelectionModel().getSelectedItem();
            if (emp == null || task == null) {
                showAlert("Error", "Please select both an employee and a task.");
                return;
            }
            tasksManagement.modifyTaskStatus(emp.getIdEmployee(), task.getIdTask());

            // update any complex tasks containing this task
            for (Task t : tasksManagement.getTasksToEmployees().get(emp)) {
                if (t instanceof ComplexTask ct) {
                    ct.updateStatus();
                }
            }
            showAlert("Success", "Task status modified.");
            // refresh the task list
            refreshTaskList(emp);
        });

        mainBox.getChildren().addAll(modifyLabel, empCombo, taskCombo, modifyButton);
        return mainBox;
    }

    private Pane getStatisticsPane() {
        VBox mainBox = new VBox(10);
        mainBox.setPadding(new Insets(10));
        mainBox.setStyle("-fx-background-color: #b6b6ef;");

        Button filterButton = new Button("Show Employees > 40 hrs");
        filterButton.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");
        TextArea filterArea = new TextArea();
        filterArea.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        filterArea.setEditable(false);
        filterArea.setPrefHeight(150);

        filterButton.setOnAction(e -> {
            List<Employee> filteredEmployees = utility.filterEmployees(tasksManagement);

            if (filteredEmployees.isEmpty()) {
                filterArea.setText("No employees found with work duration > 40 hours.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (Employee emp : filteredEmployees) {
                sb.append(emp.getName()).append(" - ")
                        .append(tasksManagement.calculateEmployeeWorkDuration(emp.getIdEmployee()))
                        .append(" hrs\n");
            }

            filterArea.setText(sb.toString());
        });

        Button statusCountButton = new Button("Show Task Status Counts");
        statusCountButton.setStyle("-fx-background-color: #c4c4ea; -fx-border-color: black; -fx-border-width: 1;");
        TextArea statusArea = new TextArea();
        statusArea.setEditable(false);
        statusArea.setStyle("-fx-background-color: #ededff; -fx-border-color: black; -fx-border-width: 1;");
        statusArea.setPrefHeight(250);

        statusCountButton.setOnAction(e -> {
            Map<String, Map<String, Integer>> counts = Utility.calculateTaskStatusCounts(tasksManagement);
            StringBuilder sb = new StringBuilder();

            counts.forEach((empName, statMap) -> {
                sb.append("Employee: ").append(empName).append("\n");
                statMap.forEach((status, count) -> {
                    sb.append("  -> ").append(status).append(" tasks: ").append(count).append("\n");
                });
            });

            statusArea.setText(sb.toString());
        });

        mainBox.getChildren().addAll(
                new Label("Filter employees with work duration > 40 hours:"),
                filterButton, filterArea,
                new Label("Task status counts per employee:"),
                statusCountButton, statusArea
        );

        return mainBox;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
