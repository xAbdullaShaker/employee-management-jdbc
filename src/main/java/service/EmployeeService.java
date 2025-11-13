// Class purpose: Business rules/validation; wraps DAO to provide a clean API.
package service;

import dao.EmployeeDAO;
import model.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService {
    private final EmployeeDAO dao = new EmployeeDAO();

    public Employee addEmployee(String name, String email, double salary) throws SQLException {
        validate(name, email, salary);
        if (dao.existsByEmail(email, null))
            throw new IllegalArgumentException("Email already exists.");
        Employee e = new Employee(name, email, salary);
        dao.create(e);
        return e;
    }

    public List<Employee> listEmployees() throws SQLException {
        return dao.findAll();
    }

    public Employee updateEmployee(int id, String name, String email, double salary) throws SQLException {
        validate(name, email, salary);

        Employee existing = dao.findById(id);
        if (existing == null)
            throw new IllegalArgumentException("Employee not found.");

        if (dao.existsByEmail(email, id))
            throw new IllegalArgumentException("Email already in use by another employee.");

        existing.setName(name);
        existing.setEmail(email);
        existing.setSalary(salary);
        dao.update(existing);
        return existing;
    }

    public boolean deleteEmployee(int id) throws SQLException {
        return dao.delete(id) > 0;
    }

    private void validate(String name, String email, double salary) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name is required.");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Valid email is required.");
        if (salary < 0)
            throw new IllegalArgumentException("Salary must be >= 0.");
    }
}
