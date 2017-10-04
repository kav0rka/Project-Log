package main;

import controllers.MainController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;


public class EmployeePane extends TitledPane{
    private TitledPane mTitledPane;
    private String mEmployee;
    private Text mNumber;
    private Text mProjectName;
    private Text mClient;
    private Text mDueDate;
    private Text mDescription;
    private Text mNote;
    private String mStatus;
    private ArrayList<Project> mEmployeeProjects;
    private ArrayList<String> mEmployees;
    private String mUserName;
    private Set<Project> mCompletedProjects;
    private DatabaseHelper mDatabaseHelper;
    private GridPane mGridPane;
    private MainController mMainController;


    public EmployeePane(String employee, ArrayList<String> employees, String user, String status, MainController mainController) {
        mDatabaseHelper = new DatabaseHelper();
        mNumber = new Text("Number");
        mProjectName = new Text("Project");
        mClient = new Text("Client");
        mDueDate = new Text("Due");
        mDescription = new Text("Description");
        mNote = new Text("Note");

        mUserName = user;
        mStatus = status;
        mEmployees = employees;

        //Get the name of current employee and set to title of TitledPane
        mEmployee = employee;
        setText(employee);
        mNumber.setId("header");
        mProjectName.setId("header");
        mClient.setId("header");
        mDueDate.setId("header");
        mDescription.setId("header");
        mNote.setId("header");

        mGridPane = new GridPane();
        mMainController = mainController;
        setupEmployeePane();
    }

    public void addEmployeeBox() {
        setContent(setupEmployeePane());
    }

    public GridPane setupEmployeePane() {


        if (mEmployee.equals("Completed")) {
            mEmployeeProjects = mDatabaseHelper.getAllProjects();
        } else {
            mEmployeeProjects = mDatabaseHelper.getProjectsByEmployee(mEmployee);
        }


        mGridPane.getChildren().removeAll(mGridPane.getChildren());
        // Create a GridPane where we can place our projects
        mGridPane.setPadding(new Insets(10,10,10,10));
        mGridPane.setHgap(20);
        mGridPane.setVgap(1);

        mGridPane.add(mNumber, 0, 0);
        mGridPane.add(mProjectName, 1, 0);
        mGridPane.add(mClient, 2, 0);
        mGridPane.add(mDueDate, 3, 0);
        //mGridPane.add(mDescription, 4, 0);
        //mGridPane.add(mNote, 5, 0);

        // Count is used to add each project to a new row
        int count = 1;
        // Add the projects for employee
        for (Project project : mEmployeeProjects) {
            if (!project.getStatus().equals(mStatus)) {
                continue;
            }

            // Prepare to check if the user can edit the project
            String employeePane = this.getText();
            // This is for each individual completed project instead of just the whole pane
            boolean viewableCompletedProject = false;
            for (String employee : mDatabaseHelper.getEmployeeAlt(project.getNumber())) {
                if (employee.equals(mUserName)) {
                    viewableCompletedProject = true;
                }
            }
            // Add the editable button if they have access, otherwise set the number as text
            MenuButton projectButton = new MenuButton();

            if (mDatabaseHelper.getEmployeeRank(mUserName).equals("Manager")
                    || employeePane.equals(mUserName)
                    || (employeePane.equals("Completed"))
                        && viewableCompletedProject) {

                // Prepare status so we can set
                MenuItem menuStatus = new MenuItem();

                // If project is open, allow to set to complete
                if (mStatus.equals("open")) {
                    menuStatus.setText("Mark as complete");
                    menuStatus.setOnAction(event -> {
                        mDatabaseHelper.updateStatus(project.getNumber(), "complete");
                        mMainController.updateUI();
                    });
                    // If open, allow to add a task
                    MenuItem menuTask = new MenuItem();
                    menuTask.setText("Add task");
                    menuTask.setOnAction(event -> {
                                Dialog<ProjectTask> assignTaskDialog = new AddTaskDialog(project, mUserName, mEmployees);
                                Optional<ProjectTask> result = assignTaskDialog.showAndWait();
                                if (result.isPresent()) {
                                    System.out.println("Adding task...");
                                    mDatabaseHelper.addTask(result.get());
                                }
                                mMainController.updateUI();
                            }
                        );
                    projectButton.getItems().add(menuTask);

                } else {
                    // If project is complete, allow user to set it to open
                    menuStatus.setText("Mark as open");
                    menuStatus.setOnAction(event -> {
                        mDatabaseHelper.updateStatus(project.getNumber(), "open");
                        mMainController.updateUI();
                    });
                }

                MenuItem menuAssign = new MenuItem("Assigned to");
                menuAssign.setOnAction(event -> {
                    Dialog<Set<String>> assignEmployeeDialog = new AssignDialog(project, mEmployees, mUserName);
                    Optional<Set<String>> result = assignEmployeeDialog.showAndWait();
                            result.ifPresent(strings -> {
                                mDatabaseHelper.updateEmployee(project.getNumber(), strings);
                            });
                    mMainController.updateUI();
                    }
                );
                // User can edit, add menu items to project button
                projectButton.getItems().add(0, menuStatus);
                projectButton.getItems().add(1, menuAssign);
                projectButton.setText(project.getNumber());
                mGridPane.add(projectButton, 0, count);
            } else {
                // User does not have access to edit project, just show it like normal
                mGridPane.add(new Text(project.getNumber()), 0, count);
            }

            mGridPane.add(new Text(project.getProjectName()), 1, count);
            mGridPane.add(new Text(project.getClient()), 2, count);
            // Allow users with access to change due date
            if (mDatabaseHelper.getEmployeeRank(mUserName).equals("Manager")
                    || employeePane.equals(mUserName)
                    || (employeePane.equals("Completed"))
                    && viewableCompletedProject) {
                // Set the format of the date in the database
                DateTimeFormatter formatOriginalDate = DateTimeFormatter.ofPattern("M/d/yyyy");
                // turn the string into a LocalDate object
                LocalDate originalDate = LocalDate.parse(project.getDate(), formatOriginalDate);
                // Make a new DatePicker with the date from database
                DatePicker datePicker = new DatePicker(originalDate);
                // Default width is way too big
                datePicker.setPrefWidth(100.);
                // Make it so they can't just type in date
                datePicker.setEditable(false);
                // Get rid of background
                datePicker.setBackground(null);
                datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                    // Get date selected, convert to proper format and save to database
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/YYYY");
                    LocalDate localDate = datePicker.getValue();
                    String date = dateTimeFormatter.format(localDate);

                    mDatabaseHelper.updateProjectDate(project.getNumber(), date);
                });
                mGridPane.add(datePicker, 3, count);
            } else {
                mGridPane.add(new Text(project.getDate()), 3, count);
            }
            count++;

            // Check for any tasks the project has
            ArrayList<ProjectTask> tasks = mDatabaseHelper.getTaskByNumber(project.getNumber());
            if (tasks.size() != 0 && project.getStatus().equals("open")) {

                TaskPane taskPane = new TaskPane(project, mMainController);
                GridPane.setColumnSpan(taskPane, 3);
                mGridPane.add(taskPane, 1, count);
                count++;
            }
        }
        return mGridPane;
    }
}
