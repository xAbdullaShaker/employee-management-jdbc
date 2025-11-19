package service;

import java.sql.SQLException;
import java.util.List;

import dao.EmployeeDAO;
import model.Employee;

/**
 * Service layer:
 * - Business Logic (validation + salary calculation)
 * - Calls DAO for DB operations
 */
public class EmployeeService {

    private final EmployeeDAO dao = new EmployeeDAO();

    // ===================== Validation Methods =====================

    // 1) Validate Name
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty.");
        }
        String trimmed = name.trim();
        if (trimmed.length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters long.");
        }
        // letters only (and spaces)
        if (!trimmed.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Name must contain letters only.");
        }
    }

    // 2) Validate Age (22 - 60)
    private void validateAge(int age) {
        if (age < 22 || age > 60) {
            throw new IllegalArgumentException("Age must be between 22 and 60.");
        }
    }

    // 3) Validate Salary (300 - 1000)
    private void validateSalary(double salary) {
        if (salary < 300 || salary > 1000) {
            throw new IllegalArgumentException("Salary must be between 300 and 1000.");
        }
    }

    // 4) validateEmployee
    public void validateEmployee(Employee e) {
        validateName(e.getName());
        validateAge(e.getAge());
        validateSalary(e.getSalary());
    }

    // ===================== Salary / Attendance Logic =====================

    // 5) Attendance-based salary deduction
    private double applyAttendanceDeduction(double baseSalary, double workedHours) {
        final double REQUIRED_HOURS = 40.0;

        if (workedHours < 0) {
            throw new IllegalArgumentException("Worked hours cannot be negative.");
        }

        if (workedHours < REQUIRED_HOURS) {
            // example: 30/40 * baseSalary
            return (workedHours / REQUIRED_HOURS) * baseSalary;
        } else {
            // no deduction
            return baseSalary;
        }
    }

    // 6) SIO 8% deduction
    private double applySioDeduction(double salary) {
        // 8% deduction
        return salary * 0.92; // or salary - salary * 0.08
    }

    // 7) Overtime calculation
    private double calculateOvertimePay(double baseSalary, double workedHours) {
        final double REQUIRED_HOURS = 40.0;
        final double OVERTIME_MULTIPLIER = 1.5;

        if (workedHours <= REQUIRED_HOURS) {
            return 0.0;
        }

        double overtimeHours = workedHours - REQUIRED_HOURS;
        double hourlyRate = baseSalary / REQUIRED_HOURS;

        return overtimeHours * hourlyRate * OVERTIME_MULTIPLIER;
    }

    // 8) Final salary calculation (attendance + SIO + overtime)
    public double calculateFinalSalary(Employee e) {
        double baseSalary = e.getSalary();
        double workedHours = e.getWorkedHours();

        // 1) Attendance deduction
        double salaryAfterAttendance = applyAttendanceDeduction(baseSalary, workedHours);

        // 2) Overtime
        double overtimePay = calculateOvertimePay(baseSalary, workedHours);

        // 3) Gross salary
        double grossSalary = salaryAfterAttendance + overtimePay;

        // 4) SIO 8% deduction
        double netSalary = applySioDeduction(grossSalary);

        return netSalary;
    }

    // ===================== Public CRUD methods (called from Main) =====================

    // Add Employee (with business logic)
    public Employee addEmployee(String name, String email, double salary,
                                int age, double workedHours) throws SQLException {

        Employee e = new Employee();
        e.setName(name);
        e.setEmail(email);
        e.setSalary(salary);   // base salary
        e.setAge(age);
        e.setWorkedHours(workedHours);

        // validate
        validateEmployee(e);

        // calculate final salary
        double finalSalary = calculateFinalSalary(e);
        e.setSalary(finalSalary);

        // save to DB
        dao.create(e);
        return e;
    }

    public List<Employee> listEmployees() throws SQLException {
        return dao.findAll();
    }

    public Employee updateEmployee(int id, String name, String email, double salary,
                                   int age, double workedHours) throws SQLException {

        Employee existing = dao.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Employee not found with id: " + id);
        }

        existing.setName(name);
        existing.setEmail(email);
        existing.setSalary(salary); // base salary
        existing.setAge(age);
        existing.setWorkedHours(workedHours);

        validateEmployee(existing);
        double finalSalary = calculateFinalSalary(existing);
        existing.setSalary(finalSalary);

        dao.update(existing);
        return existing;
    }

    public boolean deleteEmployee(int id) throws SQLException {
        int rows = dao.delete(id);
        return rows > 0;
    }
}
