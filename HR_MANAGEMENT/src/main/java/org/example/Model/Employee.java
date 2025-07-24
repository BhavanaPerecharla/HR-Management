package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private static int counter = 0;

    private int id;
    private String firstName;
    private String lastName;
    private String role;
    private String password;

    // Stores manager's ID (foreign key)
    private Integer managerId;


    private Employee manager;

    private LeaveBalance leaveBalance;
    private Contact contact;
    private Address address;
    private List<Leave> leaveApplications = new ArrayList<>();

    private void validateManagerAssignment(String role, Employee manager) {
        if (role.equalsIgnoreCase("CEO")) {
            if (manager != null) {
                throw new IllegalArgumentException("CEO cannot report to anyone.");
            }
        } else if (role.equalsIgnoreCase("HR")) {
            if (manager == null || !manager.getRole().equalsIgnoreCase("CEO")) {
                throw new IllegalArgumentException(" HR must report to a CEO.");
            }
        } else if (role.equalsIgnoreCase("Manager")) {
            if (manager == null || !manager.getRole().equalsIgnoreCase("HR")) {
                throw new IllegalArgumentException("Manager must report to an HR.");
            }
        } else if (role.equalsIgnoreCase("Team Member")) {
            if (manager == null || !manager.getRole().equalsIgnoreCase("Manager")) {
                throw new IllegalArgumentException("Team Member must report to a Manager.");
            }
        } else {
            throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    public Employee() {
        // No-arg constructor required for manual population via setters
    }

    public Employee(int id, String firstName, String lastName, String role, String password, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.password = password;
        this.managerId = managerId;
    }


    public Employee(String firstName, String lastName, String role, String password,
                    Employee manager, Contact contact, Address address) {
        this.id = counter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.password = password;
        this.manager = manager;
        validateManagerAssignment(role, manager);
        this.managerId = (manager != null) ? manager.getId() : null;

        if (this.id != 0 && this.managerId == null) {
            throw new IllegalArgumentException("Only CEO can have no manager.");
        }

        this.contact = contact;
        this.address = address;
        this.leaveBalance = new LeaveBalance(rs.getInt("employee_id"), rs.getInt("sick_leaves"), rs.getInt("paid_leaves"));
    }
    public Employee(int employeeId, String firstName, String lastName, String role,
                    String password, Contact contact, Address address) {
        this.id = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.password = password;
        this.contact = contact;
        this.address = address;
    }


    // Constructor using managerId
    public Employee(String firstName, String lastName, String role, String password,
                    Integer managerId, Contact contact, Address address) {
        this.id = counter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.password = password;
        this.managerId = managerId;

        if (this.id != 0 && managerId == null) {
            throw new IllegalArgumentException("Only CEO can have no manager.");
        }

        this.contact = contact;
        this.address = address;
        this.leaveBalance = new LeaveBalance(rs.getInt("employee_id"), rs.getInt("sick_leaves"), rs.getInt("paid_leaves")); // default leave balance
    }

    // === Getters & Setters ===

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Employee.counter = counter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
        this.manager = null; // clear manager object if ID changes
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
        this.managerId = (manager != null) ? manager.getId() : null;
    }

    public LeaveBalance getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(LeaveBalance leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Leave> getLeaveApplications() {
        return leaveApplications;
    }

    public void setLeaveApplications(List<Leave> leaveApplications) {
        this.leaveApplications = leaveApplications;
    }

    public void applyLeave(Leave leave) {
        leaveApplications.add(leave);
    }
}
