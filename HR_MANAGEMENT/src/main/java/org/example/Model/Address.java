package org.example.Model;

public class Address {
    /**
     * Represents the address details of an employee.
     */
    private int addressId;
    private String City;
    private String Locality;
    private String States;
    private String PinCode;


    public Address(int addressId,String city, String locality, String states, String pinCode) {
        this.addressId = addressId;
        this.City = city;
        this.Locality = locality;
        this.States = states;
        this.PinCode = pinCode;
    }

    public Address() {
        // Default constructor
    }


    public int getAddressId() {
        return addressId;
    }
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getStates() {
        return States;
    }

    public void setStates(String states) {
        States = states;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

}
