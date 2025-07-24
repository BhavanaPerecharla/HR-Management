package org.example.Repository;


import org.example.Model.Employee;
import org.example.Model.Leave;
import org.example.Util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for handling CRUD operations related to Leave.
 * Implements Singleton pattern for centralized access.
 */
public class LeaveRepository {

    private static final LeaveRepository instance = new LeaveRepository();
    private final LeaveBalanceRepository leaveBalanceRepository = LeaveBalanceRepository.getInstance();

    private LeaveRepository() {}

    public static LeaveRepository getInstance() {
        return instance;
    }

    /**
     * Saves a new leave request for a given employee.
     * @param employeeId The ID of the employee applying for leave
     * @param leave The Leave object containing leave details
     * @return true if the leave was successfully saved, false otherwise
     */
    public boolean save(int employeeId, Leave leave) {
        String sql = "INSERT INTO leave (employee_id, leave_type, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setString(2, leave.getType());
            stmt.setDate(3, Date.valueOf(leave.getStartDate()));
            stmt.setDate(4, Date.valueOf(leave.getEndDate()));
            stmt.setString(5, leave.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a leave by its ID.
     * @param leaveId ID of the leave
     * @return Leave object if found, null otherwise
     */
    public Leave findById(int leaveId) {
        String sql = "SELECT * FROM leave WHERE leave_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, leaveId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapFullRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds all leaves with a specific status.
     * @param status The status to filter by (e.g., "Applied", "Approved", "Rejected")
     * @return List of Leave objects with the given status
     */
    public List<Leave> findAllByStatus(String status) {
        List<Leave> list = new ArrayList<>();
        String sql = "SELECT * FROM leave WHERE status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapFullRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Finds all leave records submitted by a specific employee.
     * @param employeeId Employee ID
     * @return List of Leave records associated with the employee
     */
    public List<Leave> findByEmployeeId(int employeeId) {
        List<Leave> list = new ArrayList<>();
        String sql = "SELECT * FROM leave WHERE employee_id = ? ORDER BY start_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapFullRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Updates the status of a leave request (e.g., from "Applied" to "Approved").
     * @param leaveId ID of the leave request
     * @param newStatus New status to update to
     * @return true if the update was successful, false otherwise
     */
    public boolean updateStatus(int leaveId, String newStatus) {
        String sql = "UPDATE leave SET status = ? WHERE leave_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, leaveId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Maps a ResultSet row to a Leave object.
     * This is a helper method used in multiple retrieval operations.
     * @param rs ResultSet object
     * @return Mapped Leave object
     */
    private Leave mapFullRow(ResultSet rs) throws SQLException {
        Leave leave = new Leave();
        leave.setLeaveId(rs.getInt("leave_id"));
        leave.setEmployeeId(rs.getInt("employee_id"));
        leave.setType(rs.getString("leave_type"));
        leave.setStartDate(rs.getDate("start_date").toLocalDate());
        leave.setEndDate(rs.getDate("end_date").toLocalDate());
        leave.setStatus(rs.getString("status"));
        return leave;
    }

    /**
     * Retrieves all pending leave requests (status = 'Applied') submitted to a specific manager.
     * @param managerId The manager's employee ID
     * @return List of Leave requests pending approval
     */

    public List<Leave> getPendingLeavesByManagerId(int managerId) {
        String sql = """
        SELECT l.leave_id, l.employee_id, l.leave_type, l.start_date, l.end_date, l.status,
               e.first_name, e.last_name
        FROM leave l
        JOIN employee e ON l.employee_id = e.employee_id
        WHERE l.status = 'Applied' AND e.manager_id = ?
    """;

        List<Leave> pendingLeaves = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Leave leave = new Leave();
                    leave.setLeaveId(rs.getInt("leave_id"));
                    leave.setEmployeeId(rs.getInt("employee_id"));
                    leave.setType(rs.getString("leave_type"));
                    leave.setStartDate(rs.getDate("start_date").toLocalDate());
                    leave.setEndDate(rs.getDate("end_date").toLocalDate());
                    leave.setStatus(rs.getString("status"));

                    Employee emp = new Employee();
                    emp.setId(rs.getInt("employee_id"));
                    emp.setFirstName(rs.getString("first_name"));
                    emp.setLastName(rs.getString("last_name"));

                    leave.setEmployee(emp);

                    pendingLeaves.add(leave);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching pending leaves: " + e.getMessage());
        }

        return pendingLeaves;
    }

}
