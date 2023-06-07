package com.db;

import com.model.Employee;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DummyDB {

    private static Map<Long, Employee> db = new HashMap<>();
    private static Connection conn = null;
    private static Statement statement;
    private static PreparedStatement prepareStatement;
    private String url = "jdbc:mysql://localhost:3306/demo001";
    private String username = "demo";
    private String password = "Password@1234";

    public DummyDB()
    {
        try {
            Class driver_class = Class.forName("com.mysql.cj.jdbc.Driver");
            Driver driver = (Driver) driver_class.newInstance();
            DriverManager.registerDriver(driver);
            conn = DriverManager.getConnection(url,username,password);
            statement = conn.createStatement();
            ResultSet rs=statement.executeQuery("select * from employees");
            while(rs.next()) {
                db.put(rs.getLong("id"), new Employee(rs.getLong("id"),rs.getString("name"),rs.getString("lastname"),rs.getString("email")));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Employee insert(Employee employee) {
        String insertSQL = "INSERT INTO employees ( id, name, lastname, email) VALUES ( ?,?,?,?)";
        try {
            prepareStatement = conn.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setLong(1,employee.getId());
            prepareStatement.setString(2, employee.getName());
            prepareStatement.setString(3,employee.getLastname());
            prepareStatement.setString(4,employee.getEmail());
            int affectedRows = prepareStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                ResultSet rs = prepareStatement.getGeneratedKeys();
                if (rs.next()) {
                    db.put(employee.getId(), employee);
                    return employee;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee update(Employee employee) {
        String updateSQL = "UPDATE employees SET id = ?, name = ?, lastname = ?, email = ? WHERE  id = ?";
        try {
            prepareStatement = conn.prepareStatement(updateSQL,Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setLong(1,employee.getId());
            prepareStatement.setString(2, employee.getName());
            prepareStatement.setString(3,employee.getLastname());
            prepareStatement.setString(4,employee.getEmail());
            prepareStatement.setLong(5,employee.getId());
            int affectedRows = prepareStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                ResultSet rs = prepareStatement.getGeneratedKeys();
                if (rs.next()) {
                    db.put(employee.getId(), employee);
                    return employee;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(Long id) {
        String deleteSQL = "DELETE FROM employees WHERE  id = ?";
        try {
            prepareStatement = conn.prepareStatement(deleteSQL,Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setLong(1,id);
            int affectedRows = prepareStatement.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                ResultSet rs = prepareStatement.getGeneratedKeys();
                if (rs.next()) {
                    db.remove(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Employee getById(Long id) {
        return db.get(id);
    }

    public List<Employee> getAll() {
        try {
            ResultSet rs = statement.executeQuery("select * from employees");
            while (rs.next()) {
                db.put(rs.getLong("id"), new Employee(rs.getLong("id"), rs.getString("name"), rs.getString("lastname"), rs.getString("email")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return db.values().stream().collect(Collectors.toList());
    }

    public boolean exists(Long id) {
        return db.containsKey(id);
    }

}