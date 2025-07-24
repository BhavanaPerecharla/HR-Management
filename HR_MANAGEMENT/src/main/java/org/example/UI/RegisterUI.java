package org.example.UI;

import org.example.Model.Address;
import org.example.Model.Contact;
import org.example.Model.Employee;
import org.example.Service.EmployeeService;
import org.example.Util.InputReader;
import org.example.Util.PasswordUtil;

import java.util.List;
import java.util.Scanner;

public class RegisterUI {

    public static void showRegisterOptions(Scanner sc) {
        System.out.println("\n📝 ===== Register New User =====");

        String firstName = InputReader.readValidatedName(sc, "Enter First Name: ");
        String lastName = InputReader.readValidatedName(sc, "Enter Last Name: ");
        String email = InputReader.readValidatedEmail(sc, "Enter Email: ");
        String phone = InputReader.readValidatedPhone(sc, "Enter Phone (10 digits): ");


        String password, confirmPassword;

        while (true) {
            password = InputReader.readValidatedPassword(sc, "Enter Password: ");
            confirmPassword = InputReader.readValidatedPassword(sc, "Re-enter Password: ");

            if (!password.equals(confirmPassword)) {
                System.out.println("❌ Passwords do not match. Please try again.");
            } else if (!PasswordUtil.isValidPassword(password)) {
                System.out.println("❌ Password must be at least 10 characters, include 1 uppercase, 1 number, and 1 special character.");
            } else {
                break;
            }
        }



        String hashedPassword = PasswordUtil.hashPassword(password);
        String role = InputReader.readValidatedRole(sc, "Enter Role (CEO / HR / Manager / Team Member): ");
        Integer managerId = null;

        // Role-based manager assignment
        switch (role) {
            case "CEO" -> {
                if (EmployeeService.doesCEOExist()) {
                    System.out.println("❌ CEO already exists. Cannot register another CEO.");
                    return;
                }
                managerId = 1;
            }


                case "HR" -> {
                    List<Employee> ceos = EmployeeService.findByRole("CEO");
                    if (ceos.isEmpty()) {
                        System.out.println("❌ CEO must be onboarded before adding an HR.");
                        return;
                    }
                    Employee ceo = ceos.get(0); // Get the first CEO
                    managerId = ceo.getId();
                    System.out.printf("✅ CEO found. HR will report to CEO (ID: %d)%n", managerId);
                }


                case "Manager" -> {
                List<Employee> hrList = EmployeeService.findByRoleList("HR");
                if (hrList.isEmpty()) {
                    System.out.println("❌ No HRs available to assign as manager.");
                    return;
                }

                System.out.println("\n📋 Available HRs:");
                for (Employee hr : hrList) {
                    System.out.printf("🔹 ID: %d | %s %s\n", hr.getId(), hr.getFirstName(), hr.getLastName());
                }

                managerId = InputReader.readInt(sc, "Enter HR ID to assign as Manager: ");
                Employee selectedHR = EmployeeService.findEmployeeById(managerId);
                if (selectedHR == null || !selectedHR.getRole().equalsIgnoreCase("HR")) {
                    System.out.println("❌ Invalid HR ID.");
                    return;
                }
            }

            case "Team Member" -> {
                List<Employee> mgrList = EmployeeService.findByRoleList("Manager");
                if (mgrList.isEmpty()) {
                    System.out.println("❌ No Managers available to assign.");
                    return;
                }

                System.out.println("\n📋 Available Managers:");
                for (Employee mgr : mgrList) {
                    System.out.printf("🔹 ID: %d | %s %s\n", mgr.getId(), mgr.getFirstName(), mgr.getLastName());
                }

                managerId = InputReader.readInt(sc, "Enter Manager ID to assign as Manager: ");
                Employee selectedMgr = EmployeeService.findEmployeeById(managerId);
                if (selectedMgr == null || !selectedMgr.getRole().equalsIgnoreCase("Manager")) {
                    System.out.println("❌ Invalid Manager ID.");
                    return;
                }
            }

            default -> {
                System.out.println("❌ Invalid role selection.");
                return;
            }
        }

        // Address and Contact
        System.out.println("\n📍 ===== Address Details =====");
        Address address = InputReader.readAddress(sc);
        Contact contact = new Contact(0, email, phone);

        boolean confirm = InputReader.confirmYesNo(sc, "Are you sure you want to register this user?: ");
        if (!confirm) {
            System.out.println("❌ Registration cancelled.");
            return;
        }

        // Create and save user
        Employee newUser = new Employee(firstName, lastName, role, hashedPassword, managerId, contact, address);

        boolean success = EmployeeService.registerEmployee(newUser, hashedPassword);

        if (success) {
            System.out.printf("✅ User %s %s registered successfully as %s!%n", firstName, lastName, role);
        } else {
            System.out.println("❌ Registration failed.");
        }
    }
}
