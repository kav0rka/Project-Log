package main;

public class ProjectTask {
    private String mNumber;
    private String mTaskTitle;
    private String mTaskDescription;
    private String mTaskAssignTo;
    private String mDueDate;
    private String mStatus;


    // Default constructor
    public ProjectTask() {

    }

    public ProjectTask(String number, String title, String assignedTo, String due, String description, String status) {
        mNumber = number;
        mTaskTitle = title;
        mTaskDescription = description;
        mTaskAssignTo = assignedTo;
        mDueDate = due;
        mStatus = status;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getTaskTitle() {
        return mTaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        mTaskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return mTaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        mTaskDescription = taskDescription;
    }

    public String getTaskAssignTo() {
        return mTaskAssignTo;
    }

    public void setTaskAssignTo(String taskAssignTo) {
        mTaskAssignTo = taskAssignTo;
    }

    public String getDueDate() {
        return mDueDate;
    }

    public void setDueDate(String dueDate) {
        mDueDate = dueDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
