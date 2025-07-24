package org.example.UI;

import org.example.Model.Employee;
import org.example.Service.LoginService;

import java.util.Scanner;

public class LoginUI {

    public static void performLogin(Scanner sc) {
        System.out.println("\n🔐 ===== Login Page =====");

        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = sc.nextLine().trim();

        // Authenticate and get Employee object
        Employee employee = LoginService.authenticate(email, password);

        if (employee != null) {
            System.out.println("✅ Login successful! Welcome " + employee.getFirstName() + " (" + employee.getRole() + ")");

            // Route based on role
            switch (employee.getRole().toLowerCase()) {
                case "hr":
                  HRDashboard.showDashboard(employee,employee);
                    break;
                case "manager":
                 ManagerDashBoard.showDashboard(employee);
                    break;
                case "team member":
                  EmployeeDashboard.showDashboard(employee);
                    break;
                default:
                    System.out.println("⚠️ Unknown role: " + employee.getRole());
            }
        } else {
            System.out.println("❌ Login failed. Invalid email or password.");
        }
    }
}
