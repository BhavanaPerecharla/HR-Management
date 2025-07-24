package org.example.UI;




import org.example.Model.Employee;
import org.example.Service.EmployeeService;
import org.example.Util.InputReader;

import java.util.List;
import java.util.Scanner;

public class ManagerDashBoard {

    public static void showDashboard(Employee manager) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== 📋 Manager Dashboard =====");
            System.out.println("1. View My Profile");
            System.out.println("2. Apply for Leave");
            System.out.println("3. View My Leave Applications");
            System.out.println("4. Approve/Reject Team Member Leaves");
            System.out.println("5. List My Reportees");
            System.out.println("0. Logout");

            int choice = InputReader.readInt(sc, "Enter your choice: ");

            switch (choice) {
                case 1 -> viewProfile(manager);
                case 2 -> LeaveUI.applyLeave(sc, manager);
                case 3 -> LeaveUI.viewMyLeaves(manager);
                case 4 -> LeaveUI.handlePendingLeaves(manager);
                case 5 -> listReportees(manager);
                case 0 -> {
                    System.out.println("👋 Logging out. Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private static void viewProfile(Employee emp) {
        System.out.println("\n👤 ==== Your Profile ====\n");
        System.out.println("ID        : " + emp.getId());
        System.out.println("Name      : " + emp.getFirstName() + " " + emp.getLastName());
        System.out.println("Role      : " + emp.getRole());
        System.out.println("Email     : " + emp.getContact().getEmail());
        System.out.println("Phone     : " + emp.getContact().getPhoneNumber());
        System.out.println("Manager ID: " + emp.getManagerId());
        System.out.println("Address   : " + emp.getAddress().getCity() + ", " + emp.getAddress().getStates());
    }

    private static void listReportees(Employee manager) {
        List<Employee> reportees = EmployeeService.getReportees(manager.getId());

        if (reportees.isEmpty()) {
            System.out.println("📬 No reportees found.");
            return;
        }

        System.out.println("\n📋 Team Members Reporting to You:");
        for (Employee emp : reportees) {
            System.out.printf("🔹 ID: %d | Name: %s %s | Role: %s%n",
                    emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getRole());
        }
    }
}
