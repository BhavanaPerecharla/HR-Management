package org.example.Repository;


import org.example.Model.Contact;
import org.example.Util.DBConnection;

import java.sql.*;

public class ContactRepository {

    // Singleton instance of ContactRepository
    private static final ContactRepository instance = new ContactRepository();

    // Private constructor to prevent instantiation
    private ContactRepository() {}

    public static ContactRepository getInstance() {

        return instance;
    }

    /**
     * Saves a new contact to the database and returns the generated contact ID.
     *
     * @param contact the Contact object containing email and phone
     * @return the generated contact_id
     * @throws SQLException if insertion fails or DB error occurs
     */


    public int save(Contact contact) throws SQLException {
        // SQL insert query with RETURNING clause to get generated ID
        String sql = "INSERT INTO contact (email, phone) VALUES (?, ?) RETURNING contact_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contact.getEmail());
            stmt.setString(2, contact.getPhoneNumber());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("contact_id");
            }
        }

        throw new SQLException("Contact insert failed.");
    }
}
