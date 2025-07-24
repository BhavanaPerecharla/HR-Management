package org.example.Model;

/**
 * Represents the leave balance of an employee.
 * This class manages the sick and paid leaves available to an employee.
 */
public class LeaveBalance {
    private int balanceId;      // Unique identifier for the leave balance record
    private int employeeId;     // Associated employee's ID
    private int sickLeaves;     // Number of sick leaves available
    private int paidLeaves;     // Number of paid leaves available

    /**
     * Default constructor that initializes sick and paid leaves with default values.
     */
    public LeaveBalance(int employeeId, int sickLeaves, int paidLeaves) {
        this.sickLeaves = 12;  // Default sick leaves
        this.paidLeaves = 10;  // Default paid leaves
    }

    // === Getters and Setters ===

    public int getBalanceId() {
        return balanceId;
    }


    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }


    public int getEmployeeId() {
        return employeeId;
    }


    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }


    public int getSickLeaves() {
        return sickLeaves;
    }

    public void setSickLeaves(int sickLeaves) {
        this.sickLeaves = sickLeaves;
    }


    public int getPaidLeaves() {
        return paidLeaves;
    }


    public void setPaidLeaves(int paidLeaves) {
        this.paidLeaves = paidLeaves;
    }


    public boolean hasEnoughLeave(String type, int days) {
        return switch (type.toLowerCase()) {
            case "sick" -> sickLeaves >= days;
            case "paid" -> paidLeaves >= days;
            default -> false;
        };
    }


    public void deductLeave(String type, int days) {
        if (type.equalsIgnoreCase("sick")) {
            sickLeaves -= days;
        } else if (type.equalsIgnoreCase("paid")) {
            paidLeaves -= days;
        }
    }


    @Override
    public String toString() {
        return "LeaveBalance{" +
                "balanceId=" + balanceId +
                ", employeeId=" + employeeId +
                ", sickLeaves=" + sickLeaves +
                ", paidLeaves=" + paidLeaves +
                '}';
    }
}
