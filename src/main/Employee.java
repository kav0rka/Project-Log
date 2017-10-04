package main;

import java.util.ArrayList;

public class Employee {
    private String mName;
    private String mLoginUser;
    private String mLoginPass;
    private String mRank;
    private ArrayList<String> mViewableEmployees;


    public Employee(String name) {
        DatabaseHelper database = new DatabaseHelper();
        mName = name;
        mRank = database.getEmployeeRank(name);
        mViewableEmployees = database.getEmployeeViewable(name);
        mLoginUser = database.getEmployeeLogin(name);
        mLoginPass = database.getEmployeePassword(name);
    }

    public String getName() {
        return mName;
    }

    public String getLoginUser() {
        return mLoginUser;
    }

    public String getLoginPass() {
        return mLoginPass;
    }

    public String getRank() {
        return mRank;
    }

    public ArrayList<String> getViewableEmployees() {
        return mViewableEmployees;
    }

}
