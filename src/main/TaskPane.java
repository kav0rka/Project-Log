package main;

import controllers.MainController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class TaskPane extends TitledPane {
    private GridPane mGridPane;
    private DatabaseHelper mDatabaseHelper;
    private MainController mMainController;

    public TaskPane(Project project, MainController mainController) {
        setText("Tasks");
        mDatabaseHelper = new DatabaseHelper();
        mGridPane = new GridPane();
        mGridPane.setPadding(new Insets(5,5,5,5));
        mGridPane.setHgap(20);
        mGridPane.setVgap(1);

        // Setup text for header
        Text projectTask = new Text("ProjectTask");
        Text description = new Text("Description");
        Text due = new Text("Due");
        Text status = new Text("status");
        projectTask.setId("header");
        description.setId("header");
        due.setId("header");
        status.setId("header");


        mGridPane.add(projectTask, 0, 0);
        mGridPane.add(description, 1, 0);
        mGridPane.add(due, 2, 0);
        mGridPane.add(status, 3, 0);
        setupGridPane(project);
        setContent(mGridPane);
        mMainController = mainController;
    }

    private void setupGridPane(Project project) {
        int count = 1;
        ArrayList<ProjectTask> tasks = mDatabaseHelper.getTaskByNumber(project.getNumber());
        if (tasks.size() != 0) {
            for (ProjectTask task : tasks) {
                // Create button with task name so user can modify task
                MenuButton taskMenu = new MenuButton(task.getTaskTitle());
                MenuItem taskDeleteItem = new MenuItem("Delete");
                MenuItem taskStatusItem = new MenuItem();
                if (task.getStatus().equals("open")) {
                    taskStatusItem.setText("Mark as complete");
                    taskStatusItem.setOnAction(event -> {
                        mDatabaseHelper.updateTaskStatus(task.getNumber(), task.getTaskTitle(), "complete");
                        mMainController.updateUI();
                    });
                } else {
                    taskStatusItem.setText("Mark as open");
                    taskStatusItem.setOnAction(event -> {
                        mDatabaseHelper.updateTaskStatus(task.getNumber(), task.getTaskTitle(), "open");
                        mMainController.updateUI();
                    });
                }
                taskDeleteItem.setOnAction(event -> {
                    mDatabaseHelper.removeTask(task.getNumber(), task.getTaskTitle());
                    mMainController.updateUI();
                });

                taskMenu.getItems().add(taskStatusItem);
                taskMenu.getItems().add(taskDeleteItem);

                mGridPane.add(taskMenu, 0, count);
                mGridPane.add(new Text(task.getTaskDescription()), 1, count);
                mGridPane.add(new Text(task.getDueDate()), 2, count);
                mGridPane.add(new Text(task.getStatus()), 3, count);
                count++;
            }
        }
    }
}
