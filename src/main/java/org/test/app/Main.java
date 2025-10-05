package org.test.app;

import org.test.dao.UserDao;
import org.test.dao.UserDaoImpl;
import org.test.model.User;
import org.test.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createUser();
                case "2" -> findUserById();
                case "3" -> listAllUsers();
                case "4" -> updateUser();
                case "5" -> deleteUser();
                case "0" -> {
                    running = false;
                    System.out.println("Shutting down...");
                }
                default -> System.out.println("Wrong choice. Try again!");
            }
        }

        HibernateUtil.shutdown();
    }

    private static void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Create user");
        System.out.println("2. Find user by ID");
        System.out.println("3. Show all users");
        System.out.println("4. Update user");
        System.out.println("5. Delete user");
        System.out.println("0. Exit");
        System.out.print(": ");
    }

    private static void createUser() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        userDao.create(user);
    }

    private static void findUserById() {
        System.out.print("ID: ");
        long id = Long.parseLong(scanner.nextLine());

        User user = userDao.findById(id);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void listAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Empty.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.print("ID to update: ");
        long id = Long.parseLong(scanner.nextLine());

        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("New name (" + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) user.setName(name);

        System.out.print("New email (" + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isBlank()) user.setEmail(email);

        System.out.print("New age (" + user.getAge() + "): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isBlank()) user.setAge(Integer.parseInt(ageInput));

        userDao.update(user);
    }

    private static void deleteUser() {
        System.out.print("ID to delete: ");
        long id = Long.parseLong(scanner.nextLine());

        User user = userDao.findById(id);
        if (user != null) {
            userDao.delete(user);
        } else {
            System.out.println("User not found.");
        }
    }
}
