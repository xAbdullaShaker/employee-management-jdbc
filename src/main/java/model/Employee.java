package model;

public class Employee {
    private int id;
    private String name;
    private String email;
    private double salary;

    private int age;
    private double workedHours;

    public Employee() {}

    public Employee(int id, String name, String email, double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.salary = salary;
    }

    public Employee(String name, String email, double salary) {
        this(0, name, email, salary);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getWorkedHours() { return workedHours; }
    public void setWorkedHours(double workedHours) { this.workedHours = workedHours; }

    @Override
    public String toString() {
        return String.format(
                "Employee{id=%d, name='%s', email='%s', salary=%.2f, age=%d, workedHours=%.2f}",
                id, name, email, salary, age, workedHours
        );
    }
}
