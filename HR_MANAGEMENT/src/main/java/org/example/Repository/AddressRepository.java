package org.example.Repository;



import org.example.Model.Address;

import org.example.Util.DBConnection;

import java.sql.*;


/**
 * Repository class responsible for performing database operations
 * related to the Address entity.
 * Follows the Singleton pattern for consistent access.
 */

public class AddressRepository {

    private static final AddressRepository instance = new AddressRepository();

    private AddressRepository() {

    }

    /**
     * Returns the single instance of the AddressRepository.
     * @return the singleton instance
     */

    public static AddressRepository getInstance() {
        return instance;
    }

    /**
     * Saves the given Address object to the database and returns the generated address_id.
     *
     * @param address the Address object containing city, locality, state, and pin code
     * @return the generated address_id from the database
     * @throws SQLException if a database error occurs or insertion fails
     */


    public int save(Address address) throws SQLException {
        String sql = "INSERT INTO address (city, locality, states, pin_code) VALUES (?, ?, ?, ?) RETURNING address_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address.getCity());
            stmt.setString(2, address.getLocality());
            stmt.setString(3, address.getStates());
            stmt.setString(4, address.getPinCode());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("address_id");
            }
        }

        throw new SQLException("Address insert failed.");
    }
}
