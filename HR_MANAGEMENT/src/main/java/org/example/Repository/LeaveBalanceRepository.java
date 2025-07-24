package org.example.Repository;

import org.example.Model.LeaveBalance;
import org.example.Util.DBConnection;

import java.sql.*;


import java.util.Optional;


public class LeaveBalanceRepository {

    private static final LeaveBalanceRepository instance = new LeaveBalanceRepository();

    private LeaveBalanceRepository() {}

    public static LeaveBalanceRepository getInstance() {
        return instance;
    }

    /**
     * Inserts a new leave balance record for an employee.
     *
     * @param balance LeaveBalance object containing employee ID and initial balances
     */
    public void save(LeaveBalance balance) {
        String sql = "INSERT INTO leave_balance (employee_id, sick_leave, paid_leave) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, balance.getEmployeeId());
            stmt.setInt(2, balance.getSickLeaves());
            stmt.setInt(3, balance.getPaidLeaves());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error saving leave balance: " + e.getMessage());
        }
    }

    /**
     * Fetches the leave balance for a given employee.
     *
     * @param employeeId the ID of the employee
     * @return Optional containing LeaveBalance if found, or empty if not found
     */
    public Optional<LeaveBalance> findByEmployeeId(int employeeId) {
        String sql = "SELECT * FROM leave_balance WHERE employee_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LeaveBalance balance = new LeaveBalance(
                        rs.getInt("employee_id"),
                        rs.getInt("sick_leaves"),
                        rs.getInt("paid_leaves")
                );
                return Optional.of(balance);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching leave balance: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Updates the leave balance for a given employee.
     *
     * @param balance LeaveBalance object with updated leave values
     * @return true if update was successful, false otherwise
     */
    public boolean update(LeaveBalance balance) {
        String sql = "UPDATE leave_balance SET sick_leave = ?, paid_leave = ? WHERE employee_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, balance.getSickLeaves());
            stmt.setInt(2, balance.getPaidLeaves());
            stmt.setInt(3, balance.getEmployeeId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error updating leave balance: " + e.getMessage());
        }

        return false;
    }

    /**
     * Deletes the leave balance record for an employee.
     *
     * @param employeeId the ID of the employee
     */
    public void deleteByEmployeeId(int employeeId) {
        String sql = "DELETE FROM leave_balance WHERE employee_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error deleting leave balance: " + e.getMessage());
        }
    }
}
