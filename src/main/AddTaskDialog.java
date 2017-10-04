package main;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class AddTaskDialog extends Dialog<ProjectTask> {
    private String mUserName;
    private ArrayList<String> mEmployees;
    private Project mProject;



    public AddTaskDialog(Project project, String userName, ArrayList<String> employees) {
        mUserName = userName;
        mEmployees = employees;
        mProject = project;


        setupAddTaskDialog();
    }

    private void setupAddTaskDialog() {
        setTitle("Add task");
        GridPane taskGridPane = new GridPane();
        taskGridPane.setVgap(10);
        taskGridPane.setPadding(new Insets(5,5,5,5));


        // Create a dropdown to select an employee from
        ComboBox employeeList = new ComboBox();
        // Add user first
        employeeList.getItems().add(mUserName);
        // After user, add all employees they can see
        employeeList.getItems().addAll(mEmployees);
        employeeList.setValue(mUserName);

        // Allow user to set the title of the task
        TextField taskTitleTextField = new TextField();
        // Allow user to set due date
        TextField taskDueDateTextField = new TextField();
        // Allow user to set the description of the task
        TextArea taskDescriptionTextArea = new TextArea();

        // Set UI for the dialog
        taskGridPane.add(new Text("Assign too: "), 0, 0);
        taskGridPane.add(employeeList, 1, 0);
        taskGridPane.add(new Text("ProjectTask"), 0, 1);
        taskGridPane.add(taskTitleTextField, 1, 1);
        taskGridPane.add(new Text("Due"), 0, 2);
        taskGridPane.add(taskDueDateTextField, 1, 2);
        taskGridPane.add(new Text("Description"), 0, 3);
        taskGridPane.add(taskDescriptionTextArea, 1, 3);

        // Add OK button
        ButtonType confirmButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().setContent(taskGridPane);
        getDialogPane().getButtonTypes().add(confirmButton);

        // Create the new task
        ProjectTask task = new ProjectTask();

        setResult(task);
        setResultConverter(buttonType -> {
            task.setNumber(mProject.getNumber());
            task.setTaskTitle(taskTitleTextField.getText());
            task.setTaskAssignTo(employeeList.getValue().toString());
            task.setDueDate(taskDueDateTextField.getText());
            task.setTaskDescription(taskDescriptionTextArea.getText());
            return task;
        });
    }
}
