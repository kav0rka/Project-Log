package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Date;
import java.util.Timer;


public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

        Parent root = loader.load();
        //LoginController mainController = loader.getController();
        //LoginController loginController = loader.getController();


        primaryStage.setTitle("Project Log");
        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add("resources/css/application.css");
        primaryStage.setScene(scene);
        primaryStage.show();



        Timer refreshTimer = new Timer();
        Date timerDate = new Date();

        /*TimerTask refreshTask = new TimerTask() {
            @Override
            public void run() {
                mainController.getProjects();
            }
        };

        refreshTimer.scheduleAtFixedRate(refreshTask, timerDate, 10 * 1000);*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}

