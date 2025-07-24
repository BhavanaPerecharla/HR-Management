package org.example.UI;


import org.example.Model.Employee;
import org.example.Model.Leave;
import org.example.Service.EmployeeService;
import org.example.Service.LeaveService;
import org.example.Util.InputReader;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LeaveUI {

    private static final LeaveService leaveService = LeaveService.getInstance();


    public static void applyLeave(Scanner sc, Employee emp) {
        String type = InputReader.readChoice(sc, "Enter leave type (Sick / Paid): ", new String[]{"Sick", "Paid"});
        LocalDate startDate = InputReader.readDate(sc, "Enter start date (YYYY-MM-DD): ");
        LocalDate endDate = InputReader.readDate(sc, "Enter end date (YYYY-MM-DD): ");

        int days = (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
        if (days <= 0) {
            System.out.println("❌ Invalid date range.");
            return;
        }

        boolean success = leaveService.applyLeave(emp, type, startDate, endDate);
        if (success) {
            System.out.println("✅ Leave applied successfully.");
        } else {
            System.out.println("❌ Failed to apply for leave.");
        }
    }

    public static void viewMyLeaves(Employee emp) {
        List<Leave> myLeaves = leaveService.getLeavesByEmployeeId(emp.getId());
        if (myLeaves.isEmpty()) {
            System.out.println("ℹ️ No leave applications found.");
        } else {
            System.out.println("📋 Your Leave Applications:");
            myLeaves.forEach(System.out::println);
        }
    }




    public static void handlePendingLeaves(Employee loggedInUser) {
        List<Leave> pendingLeaves = leaveService.getPendingLeavesForManager(loggedInUser.getId());

        if (pendingLeaves.isEmpty()) {
            System.out.println("ℹ️ No pending leave requests.");
            return;
        }

        System.out.println("📋 Pending Leave Requests:");
        for (Leave l : pendingLeaves) {
            System.out.printf("🔸 Leave ID: %d | %s %s | leave_type: %s | From: %s | To: %s | Status: %s%n",
                    l.getLeaveId(),
                    l.getEmployee().getFirstName(),
                    l.getEmployee().getLastName(),
                    l.getType(),
                    l.getStartDate(),
                    l.getEndDate(),
                    l.getStatus());
        }

        int leaveId = InputReader.readInt(new Scanner(System.in), "Enter Leave ID to Approve/Reject (or 0 to cancel): ");
        if (leaveId == 0) return;

        String decision = InputReader.readString(new Scanner(System.in), "Enter 'approve' or 'reject': ").trim().toLowerCase();
        if (!decision.equals("approve") && !decision.equals("reject")) {
            System.out.println("❌ Invalid decision.");
            return;
        }

        boolean success = leaveService.updateLeaveStatus(leaveId, decision.equals("approve") ? "Approved" : "Rejected");

        if (success) {
            System.out.println("✅ Leave status updated.");
        } else {
            System.out.println("❌ Failed to update leave status.");
        }
    }

}


