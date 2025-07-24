package org.example.Service;

import org.example.Model.Employee;
import org.example.Model.Leave;
import org.example.Repository.LeaveRepository;

import java.time.LocalDate;
import java.util.List;

public class LeaveService {

    private static final LeaveService instance = new LeaveService();
    private final LeaveRepository leaveRepository;

    private LeaveService() {
        this.leaveRepository = LeaveRepository.getInstance(); // Singleton repository
    }

    public static LeaveService getInstance() {
        return instance;
    }

    // Apply for leave
    public boolean applyLeave(Employee employee, String type, LocalDate startDate, LocalDate endDate) {
        Leave leave = new Leave(type, startDate, endDate);
        leave.setEmployeeId(employee.getId());

        boolean result = leaveRepository.save(employee.getId(), leave);
        if (result) {
            System.out.println("✅ Leave application submitted successfully.");
        } else {
            System.out.println("❌ Failed to submit leave.");
        }
        return result;
    }

    // Get all leaves applied by an employee
    public List<Leave> getLeavesByEmployeeId(int employeeId) {
        return leaveRepository.findByEmployeeId(employeeId);
    }



    // Update the status of a leave ("Approved" or "Rejected")
    public boolean updateLeaveStatus(int leaveId, String status) {
        if (!status.equalsIgnoreCase("Approved") && !status.equalsIgnoreCase("Rejected")) {
            throw new IllegalArgumentException("Invalid leave status. Only 'Approved' or 'Rejected' allowed.");
        }

        // Capitalize first letter and lowercase the rest to match DB check constraint
        String normalizedStatus = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();

        return leaveRepository.updateStatus(leaveId, normalizedStatus);
    }



    public List<Leave> getPendingLeavesForManager(int managerId) {
        return leaveRepository.getPendingLeavesByManagerId(managerId);
    }



}
