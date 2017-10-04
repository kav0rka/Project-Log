package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.DatabaseHelper;
import main.Employee;
import main.EmployeePane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML private static Employee mUser;
    @FXML private static ArrayList<String> mEmployees;
    @FXML private Menu mFileMenu;
    @FXML private MenuBar mMenuBar;
    @FXML private Button mRefreshButton;
    @FXML private VBox mVBoxMain;
    @FXML private HBox mHBoxMain;
    @FXML private ScrollPane mScrollPane;

    public MainController(String user) {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        mUser = new Employee(user);
        mEmployees = databaseHelper.getEmployeeViewable(mUser.getName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setup();
    }

    public void setUser(String user) {
        mUser = new Employee(user);

    }

    public void setup() {

        if (mVBoxMain.getChildren().size() == 1) {
            setupPanes();
        } else {
            updateUI();
        }
    }

    public void updateUI() {
        for (Node pane : mVBoxMain.getChildren()) {
            if (pane instanceof EmployeePane) {
                ((EmployeePane) pane).setContent(((EmployeePane) pane).setupEmployeePane());
            }
        }
    }

    private void setupPanes() {

        // Add user pane first
        EmployeePane userPane = new EmployeePane(mUser.getName(), mEmployees, mUser.getName(), "open", this);
        userPane.addEmployeeBox();
        mVBoxMain.getChildren().add(userPane);
        // Add panes for each employee the user can see
        for (String employee : mEmployees) {
            EmployeePane employeePane = new EmployeePane(employee, mEmployees, mUser.getName(), "open", this);
            employeePane.addEmployeeBox();
            employeePane.setExpanded(false);
            mVBoxMain.getChildren().add(employeePane);
        }

        // Create new pane at bottom to show all completed projects that have not been closed
        EmployeePane completedPane = new EmployeePane("Completed", mEmployees, mUser.getName(), "complete", this);
        completedPane.addEmployeeBox();
        mVBoxMain.getChildren().add(completedPane);
    }

    public void addProject(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addProject.fxml"));
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> controllerClass) {
                if (controllerClass == AddProjectController.class) {
                    AddProjectController controller = new AddProjectController();
                    controller.setUser(mUser.getName());
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
        Stage stage = (Stage) mRefreshButton.getScene().getWindow();


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
}