package org.test.app;

import org.test.dao.UserDaoImpl;
import org.test.service.UserService;
import org.test.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDaoImpl userDao = new UserDaoImpl();
    private static final UserService userService = new UserService(userDao);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createUser();
                case "2" -> findUserById();
                case "3" -> findAllUsers();
                case "4" -> updateUser();
                case "5" -> deleteUser();
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== User Service Menu ===");
        System.out.println("1. Create User");
        System.out.println("2. Find User by ID");
        System.out.println("3. List All Users");
        System.out.println("4. Update User");
        System.out.println("5. Delete User");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void createUser() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = userService.createUser(name, email, age);
        System.out.println("Created: " + user);
    }

    private static void findUserById() {
        System.out.print("ID: ");
        long id = Long.parseLong(scanner.nextLine());

        try {
            User user = userService.findUserById(id);
            System.out.println(user);
        } catch (RuntimeException e) {
            System.out.println("User not found.");
        }
    }

    private static void findAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.print("ID of user to update: ");
        long id = Long.parseLong(scanner.nextLine());

        try {
            User user = userService.findUserById(id);

            System.out.print("New Name (current: " + user.getName() + "): ");
            String name = scanner.nextLine();
            if (!name.isBlank()) user.setName(name);

            System.out.print("New Email (current: " + user.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isBlank()) user.setEmail(email);

            System.out.print("New Age (current: " + user.getAge() + "): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.isBlank()) user.setAge(Integer.parseInt(ageInput));

            userService.updateUser(user);
            System.out.println("Updated: " + user);
        } catch (RuntimeException e) {
            System.out.println("User not found.");
        }
    }

    private static void deleteUser() {
        System.out.print("ID of user to delete: ");
        long id = Long.parseLong(scanner.nextLine());

        try {
            userService.deleteUser(id);
            System.out.println("User deleted.");
        } catch (RuntimeException e) {
            System.out.println("User not found.");
        }
    }
}
