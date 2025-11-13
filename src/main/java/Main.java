// Class purpose: Console UI (menu) to call the Service and perform CRUD operations.

import model.Employee;
import service.EmployeeService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final EmployeeService service = new EmployeeService();

    public static void main(String[] args) {
        System.out.println("=== Employee Management (JDBC) ===");
        while (true) {
            showMenu();
            int c = readInt("Choose: ");
            try {
                switch (c) {
                    case 1 -> add();
                    case 2 -> list();
                    case 3 -> update();
                    case 4 -> deleteEmp();
                    case 5 -> { System.out.println("Bye!"); return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("[Validation] " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("[SQL] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Error] " + e.getMessage());
            }
        }
    }

    private static void showMenu() {
        System.out.println("""
                ---------------------------
                1) Add Employee
                2) View All
                3) Update Employee
                4) Delete Employee
                5) Exit
                """);
    }

    private static void add() throws SQLException {
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        double salary = readDouble("Salary: ");
        Employee e = service.addEmployee(name, email, salary);
        System.out.println("Created: " + e);
    }

    private static void list() throws SQLException {
        List<Employee> all = service.listEmployees();
        if (all.isEmpty()) { System.out.println("No employees."); return; }
        all.forEach(System.out::println);
    }

    private static void update() throws SQLException {
        int id = readInt("ID to update: ");
        String name = readLine("New name: ");
        String email = readLine("New email: ");
        double salary = readDouble("New salary: ");
        Employee e = service.updateEmployee(id, name, email, salary);
        System.out.println("Updated: " + e);
    }

    private static void deleteEmp() throws SQLException {
        int id = readInt("ID to delete: ");
        boolean ok = service.deleteEmployee(id);
        System.out.println(ok ? "Deleted." : "Not found.");
    }

    // Input helpers
    private static int readInt(String label) {
        while (true) {
            try {
                System.out.print(label);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer.");
            }
        }
    }
    private static double readDouble(String label) {
        while (true) {
            try {
                System.out.print(label);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }
    }
    private static String readLine(String label) {
        System.out.print(label);
        return sc.nextLine().trim();
    }
}
