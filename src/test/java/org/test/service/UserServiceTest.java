package org.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.dao.UserDao;
import org.test.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    void createUser_ValidData_CreatesUser() {
        User created = userService.createUser("Alice", "alice@test.com", 25);

        assertNotNull(created);
        assertEquals("Alice", created.getName());
        assertEquals("alice@test.com", created.getEmail());
        assertEquals(25, created.getAge());
        assertNotNull(created.getCreatedAt());

        verify(userDao, times(1)).create(any(User.class));
    }

    @Test
    void createUser_InvalidName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                userService.createUser("", "alice@test.com", 25));
    }

    @Test
    void getAllUsers_ReturnsList() {
        User user1 = new User("Alice", "alice@test.com", 25);
        User user2 = new User("Bob", "bob@test.com", 30);
        when(userDao.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userDao, times(1)).findAll();
    }

    @Test
    void findUserById_UserExists_ReturnsUser() {
        User user = new User("Alice", "alice@test.com", 25);
        user.setId(1L);
        when(userDao.findById(1L)).thenReturn(user);

        User found = userService.findUserById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void findUserById_UserNotFound_ThrowsException() {
        when(userDao.findById(1L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.findUserById(1L));

        assertTrue(ex.getMessage().contains("User with id=1 not found"));
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void updateUser_ValidUser_CallsDaoUpdate() {
        User user = new User("Alice", "alice@test.com", 25);
        user.setId(1L);

        userService.updateUser(user);

        verify(userDao, times(1)).update(user);
    }

    @Test
    void deleteUser_ValidId_CallsDaoDelete() {
        User user = new User("Alice", "alice@test.com", 25);
        user.setId(1L);
        when(userDao.findById(1L)).thenReturn(user);

        userService.deleteUser(1L);

        verify(userDao, times(1)).delete(user);
    }
}
