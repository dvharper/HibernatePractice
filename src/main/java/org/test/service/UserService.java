package org.test.service;

import org.test.dao.UserDao;
import org.test.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public User createUser(String name, String email, int age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }

        User user = new User(name, email, age);
        user.setCreatedAt(LocalDateTime.now());
        userDao.create(user);
        return user;
    }


    public List<User> getAllUsers() {
        return userDao.findAll();
    }


    public User findUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        User user = userDao.findById(id);
        if (user == null) {
            throw new RuntimeException("User with id=" + id + " not found");
        }
        return user;
    }


    public void updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or User ID cannot be null");
        }
        userDao.update(user);
    }


    public void deleteUser(Long id) {
        User user = findUserById(id);
        userDao.delete(user);
    }
}
