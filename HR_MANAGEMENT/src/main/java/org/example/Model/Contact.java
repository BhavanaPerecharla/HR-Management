package org.example.Model;

/**
 * Represents the contact details of an employee.
 */

public class Contact {
    private int contactId;
    private String Email;
    private String PhoneNumber;
    public Contact(int anInt, String email, String phoneNumber) {
        Email = email;
        PhoneNumber = phoneNumber;
    }


    public Contact() {
        // default constructor
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}