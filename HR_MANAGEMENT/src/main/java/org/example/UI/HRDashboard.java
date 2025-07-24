package org.example.UI;



import org.example.Model.Employee;
import org.example.Service.EmployeeService;
import org.example.Util.InputReader;

import java.util.List;
import java.util.Scanner;


public class HRDashboard {



    public static void showDashboard(Employee loggedInEmployee,Employee hr) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==========================================");
            System.out.printf("🛡️ HR Dashboard - Logged in as: ID: %d | %s %s | Role: %s%n",
                    loggedInEmployee.getId(),
                    loggedInEmployee.getFirstName(),
                    loggedInEmployee.getLastName(),
                    loggedInEmployee.getRole());
            System.out.println("==========================================");
            System.out.println("1. Onboard New Employee");
            System.out.println("2. Change Manager of an Employee");
            System.out.println("3. Apply for Leave");
            System.out.println("4. View My Leave Applications");
            System.out.println("5. Approve/Reject Pending Leaves (Manager Only)");
            System.out.println("6. Search Employee by Email");
            System.out.println("7. Search Employee by Phone");
            System.out.println("8. List Reportees of a Manager");
            System.out.println("9. View My Profile");
            System.out.println("0. Exit");

            int choice = InputReader.readInt(sc, "Enter your choice: ");

            switch (choice) {
                case 1 -> RegisterUI.showRegisterOptions(sc);
                case 2 -> handleChangeManager(sc);
                case 3 -> LeaveUI.applyLeave(sc, hr);
                case 4 -> LeaveUI.viewMyLeaves(hr);
                case 5 -> LeaveUI.handlePendingLeaves(hr);
                case 6 -> SearchUI.searchByEmail(sc);
                case 7 -> SearchUI.searchByPhone(sc);
                case 8 -> handleListReportees(sc);
                case 9 -> viewProfile(hr);
                case 0 -> {
                    System.out.println("👋 Exiting HR Dashboard. Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    private static void viewProfile(Employee emp) {
        System.out.println("\n👤 My Profile:");
        System.out.printf("🔹 ID        : %d%n", emp.getId());
        System.out.printf("🔹 Name      : %s %s%n", emp.getFirstName(), emp.getLastName());
        System.out.printf("🔹 Role      : %s%n", emp.getRole());
        System.out.printf("🔹 Email     : %s%n", emp.getContact().getEmail());
        System.out.printf("🔹 Phone     : %s%n", emp.getContact().getPhoneNumber());
        System.out.printf("🔹 Address   : %s, %s, %s - %s%n",
                emp.getAddress().getLocality(),
                emp.getAddress().getCity(),
                emp.getAddress().getStates(),
                emp.getAddress().getPinCode());
        if (emp.getManager() != null) {
            System.out.printf("🔹 Manager   : %s %s (ID: %d)%n",
                    emp.getManager().getFirstName(),
                    emp.getManager().getLastName(),
                    emp.getManager().getId());
        } else {
            System.out.println("🔹 Manager   : None");
        }
    }

    private static void handleChangeManager(Scanner sc) {
        List<Employee> allEmployees = EmployeeService.getAllEmployees();
        System.out.println("\n📋 All Employees:");
        for (Employee emp : allEmployees) {
            System.out.printf("🔹 ID: %d | %s %s | Role: %s%n",
                    emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getRole());
        }
        int empId = InputReader.readInt(sc, "Enter Employee ID to update: ");
        Employee employee = EmployeeService.findEmployeeById(empId);
        if (employee == null) {
            System.out.println("❌ Employee not found.");
            return;
        }

        int newMgrId = InputReader.readInt(sc, "Enter New Manager ID: ");
        Employee newMgr = EmployeeService.findEmployeeById(newMgrId);
        if (newMgr == null) {
            System.out.println("❌ Manager not found.");
            return;
        }

        boolean updated = EmployeeService.changeManager(empId, newMgrId);
        if (updated) {
            System.out.printf("✅ %s's manager updated to %s%n", employee.getFirstName(), newMgr.getFirstName());
        } else {
            System.out.println("❌ Failed to update manager.");
        }
    }

    private static void handleListReportees(Scanner sc) {
        List<Employee> ceos = EmployeeService.findByRole("CEO");
        List<Employee> hrs = EmployeeService.findByRole("HR");
        List<Employee> managers = EmployeeService.findByRole("Manager");

        if (ceos.isEmpty() && hrs.isEmpty() && managers.isEmpty()) {
            System.out.println("📭 No employees with managerial roles found.");
            return;
        }

        System.out.println("\n🏢 Available Employees by Role:");

        if (!ceos.isEmpty()) {
            System.out.println("\n👑 CEO(s):");
            for (Employee ceo : ceos) {
                System.out.printf("🔹 ID: %d | %s %s%n", ceo.getId(), ceo.getFirstName(), ceo.getLastName());
            }
        }

        if (!hrs.isEmpty()) {
            System.out.println("\n👩‍💼 HR(s):");
            for (Employee hr : hrs) {
                System.out.printf("🔹 ID: %d | %s %s%n", hr.getId(), hr.getFirstName(), hr.getLastName());
            }
        }

        if (!managers.isEmpty()) {
            System.out.println("\n👨‍💼 Manager(s):");
            for (Employee mgr : managers) {
                System.out.printf("🔹 ID: %d | %s %s%n", mgr.getId(), mgr.getFirstName(), mgr.getLastName());
            }
        }
        int mgrId = InputReader.readInt(sc, "Enter Manager ID to view reportees: ");
        List<Employee> reportees = EmployeeService.getReportees(mgrId);
        if (reportees.isEmpty()) {
            System.out.println("📭 No reportees found.");
            return;
        }

        System.out.println("\n📋 Reportees:");
        for (Employee emp : reportees) {
            System.out.printf("🔸 ID: %d | %s %s | Role: %s%n", emp.getId(), emp.getFirstName(), emp.getLastName(), emp.getRole());
        }
    }
}
