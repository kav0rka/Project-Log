package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.DatabaseHelper;
import main.Project;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class AddProjectController implements Initializable{
    @FXML private Button mCancelButton;
    @FXML private Text mProjectName;
    @FXML private ComboBox mProjectBox;
    @FXML private ComboBox mEmployees;

    private String mUserName;

    private DatabaseHelper mDatabaseHelper;

    public AddProjectController() {
        mDatabaseHelper = new DatabaseHelper();
    }

    public void setUser(String userName) {
        mUserName = userName;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Project> projects = mDatabaseHelper.getAllProjects();
        List<String> employees = new ArrayList<>(new TreeSet<>(mDatabaseHelper.getEmployeeViewable(mUserName)));
        boolean isProject;
        for (Project project : projects) {
            isProject = true;
            for (String employee : employees) {
                if (project.getEmployeeAlt().contains(employee)) isProject = false;
                }
                if (project.getEmployeeAlt().contains(mUserName)) isProject = false;
            if (isProject) mProjectBox.getItems().add(project.getNumber());
            }
        mEmployees.getItems().add(mUserName);
        mEmployees.getItems().addAll(employees);
    }

    public void saveProject(ActionEvent actionEvent) {
        mDatabaseHelper.updateEmployee(mProjectBox.getValue().toString(), mEmployees.getValue().toString());

        backToMain();
    }

    public void cancel(ActionEvent actionEvent) {
        backToMain();
    }

    private void backToMain() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> controllerClass) {
                if (controllerClass == MainController.class) {
                    MainController controller = new MainController(mUserName);
                    //controller.setUser(userName);
                    return controller;
                } else {
                    try {
                        return controllerClass.newInstance();
                    } catch (Exception exc) {
                        throw new RuntimeException(exc); // just bail
                    }
                }
            }
        });
        Stage stage = (Stage) mProjectBox.getScene().getWindow();


        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        stage.setTitle("Project Log");
        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add("resources/css/application.css");
        stage.setScene(scene);
        stage.setScene(scene);
    }

    public void setProjectName(ActionEvent actionEvent) {
        String name = mDatabaseHelper.getProjectName(mProjectBox.getValue().toString());
        mProjectName.setText(name);
    }


}
