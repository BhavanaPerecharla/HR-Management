package org.example.Model;

import java.time.LocalDate;

/**
 * Represents a leave application for an employee.
 * This class includes details about the leave type, dates, status, and associated employee.
 */
public class Leave {
    private int leaveId;
    private int employeeId;
    private String type; // "Sick" or "Paid"
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;// "Applied", "Approved", "Rejected"
    private Employee employee;
    public Leave() {}


    public Leave(int leaveId, int employeeId, String type, LocalDate startDate, LocalDate endDate, String status) {
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;

    }

    // ✅ Custom constructor as requested
    public Leave(String type, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Applied";
    }

    /**
     * Sets the associated Employee object.
     * Typically used in service or repository layer when enriching a leave object.
     *
     * @param employee the employee to associate
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public Employee getEmployee() {
        return employee;
    }

    // Getters and Setters
    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculates the number of leave days between startDate and endDate (inclusive).
     *
     * @return Total number of leave days, or 0 if invalid dates
     */

    public int getLeaveDays() {
        if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
            return (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
        }
        return 0;
    }


    @Override

    public String toString() {
        return "Leave ID: " + leaveId +
                ", Type: " + type +
                ", Start: " + startDate +
                ", End: " + endDate +
                ", Days: " + getLeaveDays() +
                ", Status: " + status;
    }
}
