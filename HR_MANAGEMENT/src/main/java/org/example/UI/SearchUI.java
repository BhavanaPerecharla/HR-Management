package org.example.UI;


import org.example.Model.Employee;
import org.example.Service.EmployeeService;
import org.example.Util.InputReader;

import java.util.List;
import java.util.Scanner;

public class SearchUI {

    public static void searchByEmail(Scanner sc) {
        String email = InputReader.readValidatedEmail(sc, "Enter Email ID to search: ");
        Employee emp = EmployeeService.findByEmail(email);

        if (emp == null) {
            System.out.println("❌ No employee found with this email.");
            return;
        }

        displayEmployee(emp);
    }

    public static void searchByPhone(Scanner sc) {
        String phone = InputReader.readValidatedPhone(sc, "Enter Phone Number to search: ");
        Employee emp = EmployeeService.findByPhone(phone);

        if (emp == null) {
            System.out.println("❌ No employee found with this phone number.");
            return;
        }

        displayEmployee(emp);
    }



    private static void displayEmployee(Employee emp) {
        System.out.printf("""
                
                👤 Employee ID: %d
                Name: %s %s
                Role: %s
                Email: %s
                Phone: %s
                City: %s, State: %s, Pincode: %s
                Reports To: %s
                
                """,
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getRole(),
                emp.getContact().getEmail(),
                emp.getContact().getPhoneNumber(),
                emp.getAddress().getCity(),
                emp.getAddress().getStates(),
                emp.getAddress().getPinCode(),
                emp.getManagerId() == null ? "None" : emp.getManagerId()
        );
    }
}
