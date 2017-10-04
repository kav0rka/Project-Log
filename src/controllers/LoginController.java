package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.DatabaseHelper;

import java.io.IOException;

public class LoginController {

    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Button loginButton;

    private String mUserName;
    private String mPassword;
    private Button mLoginButton;

    public LoginController() {

    }




    public void login(ActionEvent event) throws IOException {
        DatabaseHelper myDb = new DatabaseHelper();
        String userName = myDb.getUserData(username.getText());
        System.out.println("User: " + userName);
        if (myDb.checkLogin(username.getText(), password.getText())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> controllerClass) {
                    if (controllerClass == MainController.class) {
                        MainController controller = new MainController(userName);
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

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            Parent root = loader.load();


            stage.setTitle("Project Log");
            Scene scene = new Scene(root, 600, 500);
            scene.getStylesheets().add("resources/css/application.css");
            stage.setScene(scene);
            stage.setScene(scene);
        }
    }
}
