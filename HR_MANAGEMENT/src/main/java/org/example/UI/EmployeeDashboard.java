package org.example.UI;


import org.example.Model.Employee;
import org.example.Service.LeaveService;
import org.example.Util.InputReader;

import java.util.Scanner;

public class EmployeeDashboard {

    public static void showDashboard(Employee employee) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== 👨 Employee Dashboard =====");
            System.out.println("1. View My Profile");
            System.out.println("2. Apply for Leave");
            System.out.println("3. View My Leave Applications");
            System.out.println("0. Logout");

            int choice = InputReader.readInt(sc, "Enter your choice: ");

            switch (choice) {
                case 1 -> viewProfile(employee);
                case 2 -> LeaveUI.applyLeave(sc, employee);
                case 3 -> LeaveUI.viewMyLeaves(employee);
                case 0 -> {
                    System.out.println("👋 Logging out. Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private static void viewProfile(Employee emp) {
        System.out.println("\n👤==== Your Profile ====");
        System.out.println("ID        : " + emp.getId());
        System.out.println("Name      : " + emp.getFirstName() + " " + emp.getLastName());
        System.out.println("Role      : " + emp.getRole());
        System.out.println("Email     : " + emp.getContact().getEmail());
        System.out.println("Phone     : " + emp.getContact().getPhoneNumber());
        System.out.println("Manager ID: " + emp.getManagerId());
        System.out.println("Address   : " + emp.getAddress().getCity() + ", " + emp.getAddress().getStates());
    }
}