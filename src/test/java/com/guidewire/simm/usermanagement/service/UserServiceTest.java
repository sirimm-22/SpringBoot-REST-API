package com.guidewire.simm.usermanagement.service;

import com.guidewire.simm.usermanagement.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserService userService; // Mock the UserService

    @InjectMocks
    private UserServiceTest userServiceTest;

    private User mockUser1;
    private User mockUser2;

    @BeforeEach
    void setUp() {
        mockUser1 = new User(1L, "Dev", "developer@gmail.com", 23);
        mockUser2 = new User(2L, "Charlie", "charlie999@gmail.com", 26);
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(mockUser1, mockUser2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("Dev", users.get(0).getName());
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser1));

        Optional<User> optionalUser = userService.getUserById(1L);
        assertTrue(optionalUser.isPresent());

        User user = optionalUser.get();
        assertEquals("Dev", user.getName());
    }

    @Test
    void testCreateUser() {
        User newUser = new User(null, "Charlie", "charlie@example.com", 40);
        User createdUser = new User(3L, "Charlie", "charlie@example.com", 40);

        when(userService.createUser(newUser)).thenReturn(createdUser);

        User result = userService.createUser(newUser);
        assertNotNull(result.getId());
        assertEquals("Charlie", result.getName());
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User(1L, "Updated Name", "updated@example.com", 30);

        when(userService.updateUser(1L, updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
    }
}
