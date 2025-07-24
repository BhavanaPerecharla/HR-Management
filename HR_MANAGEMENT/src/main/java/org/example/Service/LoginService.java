package org.example.Service;

import org.example.Model.Employee;
import org.example.Repository.EmployeeRepository;
import org.example.Util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    private static final EmployeeRepository employeeRepository = EmployeeRepository.getInstance();

    public static Employee authenticate(String email, String password) {
        try {
            Employee employee = employeeRepository.findByEmail(email);
            if (employee == null) {
                logger.warn("Login failed: No user found with email {}", email);
                return null;
            }

            String storedHashedPassword = employee.getPassword();
            boolean match = PasswordUtil.verifyPassword(password, storedHashedPassword);

            if (match) {
                logger.info("User '{}' logged in successfully.", email);
                return employee;
            } else {
                logger.warn("Login failed: Incorrect password for email {}", email);
                return null;
            }
        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage(), e);
            return null;
        }
    }
}
