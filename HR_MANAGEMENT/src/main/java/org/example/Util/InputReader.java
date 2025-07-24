package org.example.Util;

import org.example.Model.Address;
import org.example.Repository.EmployeeRepository;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utility class for reading and validating user input.
 * Provides methods for reading integers, strings, and other types of input with validation.
 */

public class InputReader {


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Prompts user to enter a valid date in yyyy-MM-dd format.
     *
     * @param sc     The Scanner object.
     * @param prompt The prompt message.
     * @return The LocalDate entered by the user.
     */
    public static LocalDate readDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter in yyyy-MM-dd format (e.g., 2025-07-23).");
            }
        }
    }
    public static String readString(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }



    public static String readChoice(Scanner sc, String prompt, String[] validOptions) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            for (String option : validOptions) {
                if (option.equalsIgnoreCase(input)) {
                    return option;
                }
            }

            System.out.println("Invalid choice. Please enter one of: " + String.join("/", validOptions));
        }
    }


    public static String readValidatedName(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Field cannot be empty.");
            } else if (!Character.isUpperCase(input.charAt(0))) {
                System.out.println("Must start with a capital letter.");
            } else {
                return input;
            }
        }
    }

    public static String readValidatedEmail(Scanner sc, String prompt) {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        while (true) {
            System.out.print(prompt);
            String email = sc.nextLine().trim();
            if (!emailPattern.matcher(email).matches()) {
                System.out.println(" Invalid email format.");
            } else {
                return email;
            }
        }
    }

    public static String readValidatedPhone(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String phone = sc.nextLine().trim();
            if (!phone.matches("\\d{10}")) {
                System.out.println(" Phone number must be exactly 10 digits.");
            } else {
                return phone;
            }
        }
    }

    public static String readValidatedRole(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String role = sc.nextLine().trim().toUpperCase();

            switch (role) {
                case "CEO" -> {
                    // Check if CEO already exists
                    if (!EmployeeRepository.getInstance().findByRole("CEO").isEmpty()) {
                        System.out.println("❌ CEO already exists. Please choose another role.");
                    } else {
                        return "CEO";
                    }
                }
                case "HR", "MANAGER", "TEAM MEMBER" -> {
                    return capitalizeRole(role);
                }
                default -> System.out.println("❌ Invalid role. Allowed roles: CEO, HR, Manager, Team Member");
            }
        }
    }


    private static String capitalizeRole(String role) {
        role = role.trim().toLowerCase();
        if (role.equals("hr")) return "HR";
        if (role.equals("manager")) return "Manager";
        if (role.equals("team member")) return "Team Member";
        return role;
    }


    public static String readValidatedPassword(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String password = sc.nextLine().trim();
            if (!PasswordUtil.isValidPassword(password)) {
                System.out.println("Password must be at least 10 characters, include 1 uppercase, 1 number, and 1 special character.");
            } else {
                return password;
            }
        }
    }

    public static Address readAddress(Scanner sc) {
        String city = readNonEmpty(sc, "Enter City: ");
        String locality = readNonEmpty(sc, "Enter Locality: ");
        String state = readNonEmpty(sc, "Enter State: ");
        String pin = readNonEmpty(sc, "Enter Pin Code: ");
        return new Address(0, city, locality, state, pin);
    }

    private static String readNonEmpty(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Field cannot be empty.");
        }
    }



    public static boolean confirmYesNo(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            else if (input.equals("n") || input.equals("no")) return false;
            else System.out.println("Please enter 'y' or 'n'.");
        }
    }
    public static int readIntInRange(Scanner sc, String prompt, int min, int max) {
        int number;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                number = Integer.parseInt(input);
                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter an integer.");
            }
        }
    }


}
