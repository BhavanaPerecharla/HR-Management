package org.example.Util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int WORKLOAD = 12;

    /**
     * Hashes a plain password using BCrypt algorithm.
     *
     * @param plainPassword the password in plain text
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORKLOAD));
    }

    /**
     * Verifies a plain password against a stored hashed password.
     *
     * @param plainPassword  the password entered by the user
     * @param hashedPassword the hashed password stored in DB
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hashed password format");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Validates whether the password meets the following requirements:
     * - Minimum 10 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     *
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null &&
                password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$");
    }




}
