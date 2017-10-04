package main;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AssignDialog extends Dialog<Set<String>>{
    private DatabaseHelper mDatabaseHelper;
    private Project mProject;
    private String[] mSavedEmployees;

    private ArrayList<String> mEmployees;
    private String mUserName;

    public AssignDialog(Project project, ArrayList<String> employees, String user) {
        mProject = project;
        mEmployees = employees;
        mUserName = user;

        mDatabaseHelper = new DatabaseHelper();
        mSavedEmployees = mDatabaseHelper.getEmployeeAlt(mProject.getNumber());
        // Setup the dialog
        setupAssignDialog();
    }


    private void setupAssignDialog() {
        setTitle("Assign employee");

        // Create a gridpane to add our checkboxes
        GridPane checkGridPane = new GridPane();
        int checkCount = 0;
        // Add checkbox for user
        CheckBox userCheckBox = new CheckBox(mUserName);
        for (String string : mSavedEmployees) {
            if (string.equals(mUserName)) {
                userCheckBox.setSelected(true);
            }
        }
        checkGridPane.add(userCheckBox, 0, checkCount);
        checkCount++;


        for (String employee : mEmployees) {
            // Create a new checkbox for each employee
            CheckBox checkBox = new CheckBox(employee);
            // Load all employees that are saved and set them as checked
            for (String string : mSavedEmployees) {
                if (string.equals(employee)) {
                    checkBox.setSelected(true);
                }
            }
            checkGridPane.add(checkBox, 0, checkCount);
            checkCount++;
        }


        Set<String> employeesToAdd = new HashSet<>();


        // Add the ok button to the dialog
        ButtonType confirmButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(confirmButton);


        // Add the check grid to the dialog
        getDialogPane().setContent(checkGridPane);


        // Setup the result to return the employees that will be assigned to the project
        setResult(employeesToAdd);
        setResultConverter(buttonType -> {
            for (Node node : checkGridPane.getChildren()) {
                if (node instanceof CheckBox) {
                    if (((CheckBox) node).isSelected()) {
                        employeesToAdd.add(((CheckBox) node).getText());
                    }
                }
            }
            return employeesToAdd;
        });
    }

}
