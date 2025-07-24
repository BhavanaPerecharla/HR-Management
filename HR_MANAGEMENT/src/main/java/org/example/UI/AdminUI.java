package org.example.UI;

import java.util.Scanner;

public class AdminUI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("=========================================");
            System.out.println("Welcome to HR Management System");
            System.out.println("[1] Login into your Account");
            System.out.println("[2] Register as New User");
            System.out.println("[0] Exit");
            System.out.print("👉 Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    LoginUI.performLogin(sc);
                    break;
                case "2":
                    RegisterUI.showRegisterOptions(sc);
                    break;
                case "0":
                    System.out.println("👋 Exiting... Goodbye!");
                    return; // Exits the program
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
            System.out.println();
        }
    }
}
