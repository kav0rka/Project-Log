package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class DatabaseHelper {

    private String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/project_log.sqlite";


    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void addTask(ProjectTask task) {
        try (Connection conn = this.connect()) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO tasks ("
                                + "NUMBER,"
                                + "NAME,"
                                + "EMPLOYEE,"
                                + "DUE,"
                                + "DESCRIPTION,"
                                + "STATUS)"
                                + "VALUES (?,?,?,?,?,?);");
                pstmt.setString(1, task.getNumber());
                pstmt.setString(2, task.getTaskTitle());
                pstmt.setString(3, task.getTaskAssignTo());
                pstmt.setString(4, task.getDueDate());
                pstmt.setString(5, task.getTaskDescription());
                pstmt.setString(6, "open");
                pstmt.executeUpdate();
                pstmt.close();
                conn.commit();
                conn.close();
            }

        }catch (SQLException e) {
            System.out.println("Error when adding task");
            System.out.println(e.getMessage());
        }
    }

    public void removeTask(String number, String title) {
        String sql = "DELETE FROM tasks WHERE NUMBER = ? AND name = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEmployee(String name, String rank, ArrayList<String> viewable, String login, String password) {
        try (Connection conn = this.connect()) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employees ("
                        + "NAME,"
                        + "RANK,"
                        + "VIEWABLE_EMPLOYEES,"
                        + "LOGIN,"
                        + "PASSWORD)"
                        + "VALUES (?,?,?,?,?);");
                String employees = "";
                for (String employee : viewable) {
                    if (!employee.equals(name)) {
                        if (employees.equals("")) {
                            employees += employee;
                        } else {
                            employees += "," + employee;
                        }
                    }
                }

                pstmt.setString(1, name);
                pstmt.setString(2, rank);
                pstmt.setString(3, employees);
                pstmt.setString(4, login);
                pstmt.setString(5, password);
                pstmt.executeUpdate();
                pstmt.close();
                conn.commit();
                conn.close();
            }
        } catch(SQLException e){
            System.out.println("Error when adding employee");
            System.out.println(e.getMessage());
        }
    }


    public ArrayList<ProjectTask> getTaskByNumber(String number) {
        String sql = "SELECT * FROM tasks WHERE NUMBER='" + number + "';";
        ArrayList<ProjectTask> tasks = new ArrayList<>();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                ProjectTask task = new ProjectTask(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6));
                tasks.add(task);
            }
        }catch (SQLException e) {
            System.out.printf("Error when getting tasks for: %s%n", number);
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    public void updateProjectDate(String number, String date) {
        String sql = "UPDATE projects SET DATE_ALT = ? WHERE NUMBER = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql) ) {

            pstmt.setString(1, date);
            pstmt.setString(2, number);

            pstmt.executeUpdate();


        }catch (SQLException e) {
            System.out.println("Error when updating date");
            System.out.println(e.getMessage());
        }

    }

    public void updateTaskStatus(String number, String name, String status) {
        String sql = "UPDATE tasks SET STATUS = ? WHERE NUMBER = ? AND NAME = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, status);
            pstmt.setString(2, number);
            pstmt.setString(3, name);

            pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
            conn.close();


        }catch (SQLException e) {
            System.out.println("Error when updating task status");
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> getEmployeeViewable(String name) {
        String sql = "SELECT * FROM employees WHERE NAME='" + name + "';";
        ArrayList<String> employees = new ArrayList<>();

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                String employeeArray[] = resultSet.getString(3).split(",");
                for (String employee : employeeArray) {
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }

    public String getEmployeeLogin(String name) {
        String sql = "SELECT * FROM employees WHERE NAME='" + name + "';";
        String login = "";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            login = resultSet.getString(4);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return login;
    }

    public String getEmployeePassword(String name) {
        String sql = "SELECT * FROM employees WHERE NAME='" + name + "';";
        String password = "";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            password = resultSet.getString(5);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return password;
    }

    public String getEmployeeRank(String name) {
        String sql = "SELECT * FROM employees WHERE NAME='" + name + "';";
        String rank = "";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            rank = resultSet.getString(2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rank;
    }

    // TODO return project
    public void getProjectByNumber(String number) {
        String sql = "SELECT * FROM projects WHERE NUMBER=" + number + ";";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("NUMBER"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String[] getEmployeeAlt(String number) {
        String sql = "SELECT * FROM projects WHERE NUMBER =" + number + ";";
        String employees[] = null;
        Set<String> employeeSet = new HashSet<>();
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
             String employee = resultSet.getString(7);
             if (employee != null) {
                 employees = employee.split(",");
                 for (String employeeString : employees) {
                     employeeSet.add(employeeString);
                 }
             }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employeeSet.toArray(new String[employeeSet.size()]);
    }

    public String getEmployeePrimary(String number) {
        String sql = "SELECT * FROM projects WHERE NUMBER =" + number + ";";
        String employee = "";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
             employee = resultSet.getString(3);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employee;
    }

    public String getProjectName(String number) {
        String sql = "Select PROJECT_NAME from projects WHERE NUMBER = " + number + ";";
        String name = "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public ArrayList<Project> getAllProjects() {
        ArrayList<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects;";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                Project project = new Project(resultSet.getString(1),
                        resultSet.getString(10),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9));
                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return projects;
    }

    public ArrayList<Project> getProjectsByEmployee(String employee) {
        ArrayList<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE EMPLOYEE_ALT LIKE '%" + employee + "%';";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                Project project = new Project(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9));
                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return projects;
    }

    public void updateEmployee(String number, String employee) {
        String sql = "UPDATE projects SET EMPLOYEE_ALT = ? WHERE number = ?";
        Set<String> employeesSet = new HashSet<>();
        String[] employeeArray = getEmployeeAlt(number);
        String employeesToSave = "";

        employeesSet.add(employee);
        if (employeeArray.length != 0) {
            for (String employeeToAdd : employeeArray) {
                employeesSet.add(employeeToAdd);
            }
        }
        for (String employeeToAdd : employeesSet) {
            if (employeesToSave.equals("")) {
                employeesToSave = employeeToAdd;
            } else {
                employeesToSave += "," + employeeToAdd;
            }
        }

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            pstmt.setString(1, employeesToSave);
            pstmt.setString(2, number);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateEmployee(String number, Set<String> employees) {
        String sql = "UPDATE projects SET EMPLOYEE_ALT = ? WHERE number = ?";
        String employeesToSave = "";

        if (employees.size() != 0) {
            for (String employeeToAdd : employees) {
                if (employeesToSave.equals("")) {
                    employeesToSave = employeeToAdd;
                } else {
                    employeesToSave += "," + employeeToAdd;
                }
            }
        } else {
            employeesToSave = getEmployeePrimary(number);
        }

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            pstmt.setString(1, employeesToSave);
            pstmt.setString(2, number);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateNote(String number, String note) {
        String sql = "UPDATE projects SET NOTES = ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            pstmt.setString(1, note);
            pstmt.setString(2, number);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void updateStatus(String number, String status) {
        String sql = "UPDATE projects SET STATUS = ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql) ) {
            pstmt.setString(1, status);
            pstmt.setString(2, number);
            System.out.printf("Status for %s set to %s%n", number, status);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeProject(String number) {
        String sql = "DELETE FROM projects WHERE NUMBER = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean checkLogin(String checkUser, String checkPass) {
        String sql = "SELECT PASSWORD FROM employees WHERE LOGIN='" + checkUser +"'COLLATE NOCASE;";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                if(resultSet.getString(1).equals(checkPass)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUserData(String login) {
        String sql = "SELECT * FROM employees WHERE LOGIN = '" + login + "'COLLATE NOCASE;";
        String user = "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
                ResultSet resultSet = stmt.executeQuery(sql);
                while (resultSet.next()) {
                    user = resultSet.getString(1);
            }

        } catch (SQLException e) {
            System.out.println("Get user failed");
            e.printStackTrace();
        }
        return user;
    }
}
