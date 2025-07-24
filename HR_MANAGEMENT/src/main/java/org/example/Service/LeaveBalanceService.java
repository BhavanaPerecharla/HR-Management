package org.example.Service;



import org.example.Model.LeaveBalance;
import org.example.Repository.LeaveBalanceRepository;

import java.util.Optional;

public class LeaveBalanceService {

    private static final LeaveBalanceService instance = new LeaveBalanceService();
    private final LeaveBalanceRepository leaveBalanceRepository = LeaveBalanceRepository.getInstance();

    private LeaveBalanceService() {}

    public static LeaveBalanceService getInstance() {
        return instance;
    }

    /**
     * Creates and saves a default leave balance for a new employee.
     * @param employeeId the employee ID
     */
    public void initializeLeaveBalance(int employeeId) {
        LeaveBalance balance = new LeaveBalance(employeeId, 10, 10); // default values
        leaveBalanceRepository.save(balance);
    }

    /**
     * Gets the leave balance of a specific employee.
     * @param employeeId the employee ID
     * @return LeaveBalance if found, otherwise null
     */
    public LeaveBalance getLeaveBalance(int employeeId) {
        Optional<LeaveBalance> optional = leaveBalanceRepository.findByEmployeeId(employeeId);
        return optional.orElse(null);
    }

    /**
     * Deducts leaves from the balance based on type.
     * @param employeeId the employee ID
     * @param leaveType "Sick" or "Paid"
     * @param days number of days to deduct
     * @return true if deduction was successful, false if insufficient balance
     */
    public boolean deductLeave(String leaveType, int employeeId, int days) {
        LeaveBalance balance = getLeaveBalance(employeeId);
        if (balance == null) return false;

        switch (leaveType.toUpperCase()) {
            case "SICK" -> {
                if (balance.getSickLeaves() < days) return false;
                balance.setSickLeaves(balance.getSickLeaves() - days);
            }
            case "PAID" -> {
                if (balance.getPaidLeaves() < days) return false;
                balance.setPaidLeaves(balance.getPaidLeaves() - days);
            }
            default -> {
                return false;
            }
        }

        return leaveBalanceRepository.update(balance);
    }

    /**
     * Adds back leave days (used in case of rejected leave).
     * @param leaveType "Sick" or "Paid"
     * @param employeeId employee ID
     * @param days number of days to add back
     */
    public void restoreLeave(String leaveType, int employeeId, int days) {
        LeaveBalance balance = getLeaveBalance(employeeId);
        if (balance == null) return;

        switch (leaveType.toUpperCase()) {
            case "SICK" -> balance.setSickLeaves(balance.getSickLeaves() + days);
            case "PAID" -> balance.setPaidLeaves(balance.getPaidLeaves() + days);
        }

        leaveBalanceRepository.update(balance);
    }

    /**
     * Deletes the leave balance record of a terminated employee.
     * @param employeeId employee ID
     */
    public void deleteLeaveBalance(int employeeId) {
        leaveBalanceRepository.deleteByEmployeeId(employeeId);
    }
}

