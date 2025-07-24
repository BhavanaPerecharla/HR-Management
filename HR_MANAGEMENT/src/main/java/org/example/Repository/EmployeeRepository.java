package org.example.Repository;

import org.example.Model.Address;
import org.example.Model.Contact;
import org.example.Model.Employee;
import org.example.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for handling all database operations related to the Employee entity.
 * Follows Singleton pattern.
 */
public class EmployeeRepository {

    private static EmployeeRepository instance;

    private EmployeeRepository() {}

    public static EmployeeRepository getInstance() {
        if (instance == null) {
            instance = new EmployeeRepository();
        }
        return instance;
    }

    /**
     * Finds all employees with a given role (case-insensitive).
     */

    public List<Employee> findByRole(String role) {
        List<Employee> employees = new ArrayList<>();
        String sql = """
        SELECT e.*, a.*, c.*
        FROM employee e
        JOIN address a ON e.address_id = a.address_id
        JOIN contact c ON e.contact_id = c.contact_id
        WHERE LOWER(e.role) = LOWER(?)
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee emp = mapResultSetToEmployee(rs);
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    /**
     * Another version to find employees by role (case-sensitive).
     */


    public List<Employee> findByRoleList(String role) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, a.*, c.* FROM employee e " +
                "JOIN address a ON e.address_id = a.address_id " +
                "JOIN contact c ON e.contact_id = c.contact_id " +
                "WHERE e.role = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee e = mapResultSetToEmployee(rs);
                employees.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return employees;
    }

    /**
     * Finds employees who report to a given manager by manager ID.
     */

    public List<Employee> findEmployeesByManagerId(int managerId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, a.*, c.* FROM employee e " +
                "JOIN address a ON e.address_id = a.address_id " +
                "JOIN contact c ON e.contact_id = c.contact_id " +
                "WHERE e.manager_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    /**
     * Checks if an employee exists by ID.
     */

    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM employee WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Finds an employee by their employee ID, including their address and contact details.
     */

    public Employee findById(int id) {
        String sql = """
        SELECT e.*, a.*, c.*
        FROM employee e
        JOIN address a ON e.address_id = a.address_id
        JOIN contact c ON e.contact_id = c.contact_id
        WHERE e.employee_id = ?
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapResultSetToEmployee(rs);
        } catch (SQLException e) {
            System.err.println("❌ Error fetching by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Finds an employee by their email (case-insensitive).
     */

    public Employee findByEmail(String email) {
        String sql = """
        SELECT e.*, a.*, c.*
        FROM employee e
        JOIN address a ON e.address_id = a.address_id
        JOIN contact c ON e.contact_id = c.contact_id
        WHERE LOWER(c.email) = LOWER(?)
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployee(rs); // Your method to build Employee object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds an employee by their phone number.
     */

    public Employee findByPhone(String phone) {
        String sql = """
        SELECT e.*, 
               c.email, c.phone, 
               a.city, a.locality, a.states, a.pin_code 
        FROM employee e
        JOIN contact c ON e.contact_id = c.contact_id
        JOIN address a ON e.address_id = a.address_id
        WHERE c.phone = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching by phone: " + e.getMessage());
        }
        return null;
    }


    /**
     * Updates an employee's manager.
     */

    public boolean updateManager(int employeeId, int newManagerId) {
        String sql = "UPDATE employee SET manager_id = ? WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newManagerId);
            stmt.setInt(2, employeeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating manager: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets list of employees reporting to a specific manager (includes address and contact).
     */

    public List<Employee> findByManagerId(int managerId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, a.*, c.* FROM employee e " +
                "JOIN address a ON e.address_id = a.address_id " +
                "JOIN contact c ON e.contact_id = c.contact_id " +
                "WHERE e.manager_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("employee_id"));
                e.setFirstName(rs.getString("first_name"));
                e.setLastName(rs.getString("last_name"));
                e.setRole(rs.getString("role"));
                e.setPassword(rs.getString("password"));
                e.setManagerId(rs.getInt("manager_id"));

                Address address = new Address(
                        rs.getInt("address_id"),
                        rs.getString("city"),
                        rs.getString("locality"),
                        rs.getString("states"),
                        rs.getString("pin_code")
                );
                e.setAddress(address);

                Contact contact = new Contact(
                        rs.getInt("contact_id"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                e.setContact(contact);

                employees.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return employees;
    }

    /**
     * Fetches all employees in the system (with address and contact).
     */

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = """
        SELECT e.*,
               c.email, c.phone,
               a.city, a.locality, a.states, a.pin_code
        FROM employee e
        LEFT JOIN contact c ON e.contact_id = c.contact_id
        LEFT JOIN address a ON e.address_id = a.address_id
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee emp = mapResultSetToEmployee(rs);
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    /**
     * Saves a new employee to the database and returns the generated employee ID.
     */

    public int save(Employee employee, String hashedPassword) {
        String sql = "INSERT INTO employee (first_name, last_name, role, password, manager_id, contact_id, address_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING employee_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getRole());
            stmt.setString(4, hashedPassword); // use hashedPassword directly
            if (employee.getManager() != null) {
                stmt.setInt(5, employee.getManager().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, employee.getContact().getContactId());
            stmt.setInt(7, employee.getAddress().getAddressId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("employee_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    private static Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Address address = new Address(
                rs.getInt("address_id"),
                rs.getString("city"),
                rs.getString("locality"),
                rs.getString("states"),
                rs.getString("pin_code")
        );

        Contact contact = new Contact(
                rs.getInt("contact_id"),
                rs.getString("email"),
                rs.getString("phone")
        );

        Employee employee = new Employee(
                rs.getInt("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("role"),
                rs.getString("password"),
                contact,
                address
        );

        // Handle nullable manager
        int managerId = rs.getInt("manager_id");
        if (!rs.wasNull()) {
            employee.setManagerId(managerId);
        }

        return employee;
    }

}
