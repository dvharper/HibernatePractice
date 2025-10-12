package org.test.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.test.model.User;
import org.test.util.HibernateUtil;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoImplTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private static UserDaoImpl userDao;

    @BeforeAll
    static void setUp() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        sessionFactory = HibernateUtil.getSessionFactory();
        userDao = new UserDaoImpl();
    }

    @Test
    @Order(1)
    void createUserTest() {
        User user = new User("Alice", "alice@test.com", 25);
        user.setCreatedAt(LocalDateTime.now());
        userDao.create(user);
        assertNotNull(user.getId());
    }

    @Test
    @Order(2)
    void findAllUsersTest() {
        List<User> users = userDao.findAll();
        assertFalse(users.isEmpty());
    }

    @Test
    @Order(3)
    void updateUserTest() {
        User user = userDao.findAll().get(0);
        user.setAge(30);
        userDao.update(user);
        User updated = userDao.findById(user.getId());
        assertEquals(30, updated.getAge());
    }

    @Test
    @Order(4)
    void deleteUserTest() {
        User user = userDao.findById(1L);
        userDao.delete(user);
        User deleted = userDao.findById(user.getId());
        assertNull(deleted);
    }
}
