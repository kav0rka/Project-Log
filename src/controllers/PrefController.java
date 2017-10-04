package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class PrefController {
    @FXML
    private Button saveButton;
    private String mUser;

    public PrefController(String user) {
        mUser = user;

    }


    public void savePreferences(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/preferences.fxml"));
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> controllerClass) {
                if (controllerClass == MainController.class) {
                    MainController controller = new MainController(mUser);
                    //controller.setUser(name);
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

        Stage stage = (Stage) saveButton.getScene().getWindow();

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        stage.setTitle("Project Log");
        Scene scene = new Scene(root, 300, 600);
        scene.getStylesheets().add("resources/css/application.css");
        stage.setScene(scene);
        stage.setScene(scene);
    }
}
