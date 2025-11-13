// Class purpose: DAO layer - runs SQL CRUD for employees using JDBC (PreparedStatement).
package dao;

import model.Employee;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // CREATE
    public int create(Employee e) throws SQLException {
        String sql = "INSERT INTO employees(name, email, salary) VALUES(?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setDouble(3, e.getSalary());

            int affected = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }
            return affected;
        }
    }

    // READ all
    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT id, name, email, salary FROM employees ORDER BY id";
        List<Employee> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDouble("salary")));
            }
        }
        return list;
    }

    // READ by id
    public Employee findById(int id) throws SQLException {
        String sql = "SELECT id, name, email, salary FROM employees WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getDouble("salary"));
                }
                return null;
            }
        }
    }

    // UPDATE
    public int update(Employee e) throws SQLException {
        String sql = "UPDATE employees SET name = ?, email = ?, salary = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setDouble(3, e.getSalary());
            ps.setInt(4, e.getId());
            return ps.executeUpdate();
        }
    }

    // DELETE
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    // Helper: check unique email (optionally excluding a specific id)
    public boolean existsByEmail(String email, Integer excludeId) throws SQLException {
        String sql = (excludeId == null)
                ? "SELECT 1 FROM employees WHERE email = ?"
                : "SELECT 1 FROM employees WHERE email = ? AND id <> ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            if (excludeId != null) ps.setInt(2, excludeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
