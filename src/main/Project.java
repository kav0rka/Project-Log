package main;

public class Project {
    // The variables are listed in the order they appear in the database
    String mNumber;
    String mDate;
    String mEmployeePrimary;
    String mProjectName;
    String mDescription;
    String mClient;
    String mEmployeeAlt;
    String mNotes;
    String mStatus;


    // Setup constructor
    public Project(String number,
                   String date,
                   String employeePrimary,
                   String projectName,
                   String description,
                   String client,
                   String employeeAlt,
                   String notes,
                   String status) {
        mNumber = number;
        mDate = date;
        mEmployeePrimary = employeePrimary;
        mProjectName = projectName;
        mDescription = description;
        mClient = client;
        mEmployeeAlt = employeeAlt;
        mNotes = notes;
        mStatus = status;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getEmployeePrimary() {
        return mEmployeePrimary;
    }

    public void setEmployeePrimary(String employeePrimary) {
        mEmployeePrimary = employeePrimary;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String projectName) {
        mProjectName = projectName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getClient() {
        return mClient;
    }

    public void setClient(String client) {
        mClient = client;
    }

    public String getEmployeeAlt() {
        return mEmployeeAlt;
    }

    public void setEmployeeAlt(String employeeAlt) {
        mEmployeeAlt = employeeAlt;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
