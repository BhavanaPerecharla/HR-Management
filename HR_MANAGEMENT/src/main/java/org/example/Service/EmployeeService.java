package org.example.Service;

import org.example.Model.*;
import org.example.Repository.*;
import org.example.Util.DBConnection;
import org.example.Util.InputReader;
import org.example.Util.PasswordUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EmployeeService {

    private static final Scanner sc = new Scanner(System.in);
    private static final EmployeeRepository employeeRepository = EmployeeRepository.getInstance();
    private static final AddressRepository addressRepository = AddressRepository.getInstance();
    private static final ContactRepository contactRepository = ContactRepository.getInstance();


    private static final EmployeeService instance = new EmployeeService();
    private static Employee loggedInEmployee;

    private EmployeeService() {}


    public static List<Employee> findByRole(String role) {
        return employeeRepository.findByRole(role);
    }


    public static List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees(); // implement this if missing
    }




    public static Employee findEmployeeById(int id) {
        return employeeRepository.findById(id);
    }





    public void logout() {
        loggedInEmployee = null;
    }

    public Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }

    // ===== Employee Lookup =====
    public static Employee getById(int employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public static Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public static Employee findByPhone(String phone) {
        return employeeRepository.findByPhone(phone);
    }

    public static List<Employee> findByRoleList(String role) {
        return employeeRepository.findByRoleList(role);
    }

    public List<Employee> getEmployeesByRole(String role) {
        return employeeRepository.findByRoleList(role);
    }

    public static List<Employee> getReportees(int managerId) {
        return employeeRepository.findByManagerId(managerId);
    }

    public static boolean isManagerExists(int managerId) {
        return employeeRepository.existsById(managerId);
    }



    // ===== Registration =====
    public static boolean registerEmployee(Employee employee, String hashedPassword) {
        try {
            int addressId = addressRepository.save(employee.getAddress());
            int contactId = contactRepository.save(employee.getContact());

            employee.getAddress().setAddressId(addressId);
            employee.getContact().setContactId(contactId);

            int employeeId = employeeRepository.save(employee, hashedPassword);
            employee.setId(employeeId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Change Manager =====
    public static boolean changeManager(int employeeId, int newManagerId) {
        Employee employee = getById(employeeId);
        Employee newManager = getById(newManagerId);

        if (employee == null || newManager == null) {
            System.out.println("❌ Invalid Employee or Manager ID.");
            return false;
        }

        if (employeeId == newManagerId) {
            System.out.println("❌ Cannot assign self as manager.");
            return false;
        }

        String empRole = employee.getRole().toUpperCase();
        String mgrRole = newManager.getRole().toUpperCase();

        if (empRole.equals("MANAGER") && !mgrRole.equals("HR")) {
            System.out.println("❌ Manager must report to HR.");
            return false;
        }

        if (empRole.equals("TEAM MEMBER") && !mgrRole.equals("MANAGER")) {
            System.out.println("❌ Team Member must report to Manager.");
            return false;
        }

        return employeeRepository.updateManager(employeeId, newManagerId);
    }



    public static boolean doesCEOExist() {
        String sql = "SELECT COUNT(*) FROM employee WHERE TRIM(LOWER(role)) = 'ceo'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
