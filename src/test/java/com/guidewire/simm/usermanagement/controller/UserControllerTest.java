package com.guidewire.simm.usermanagement.controller;

import com.guidewire.simm.usermanagement.model.User;
import com.guidewire.simm.usermanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = List.of(
                new User(1L, "siri", "siri@gmail.com", 28),
                new User(2L, "doremon", "doremon@gmail.com", 35)
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        List<User> users = userController.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void testGetUserById_Found() {
        User mockUser = new User(1L, "siri", "siri@gmail.com", 28);

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("siri", response.getBody().getName());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testCreateUser() {
        User newUser = new User(null, "Charlie", "charlie@example.com", 40);
        User savedUser = new User(3L, "Charlie", "charlie@example.com", 40);

        when(userService.createUser(Mockito.any(User.class))).thenReturn(savedUser);

        User response = userController.createUser(newUser);

        assertNotNull(response);
        assertEquals("Charlie", response.getName());
        assertEquals(3L, response.getId());
    }

    @Test
    void testUpdateUser_Found() {
        User updatedUser = new User(1L, "Updated Name", "updated@example.com", 30);

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(null);

        ResponseEntity<User> response = userController.updateUser(1L, new User(1L, "Test", "test@example.com", 25));

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteUser_Success() {
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userService.deleteUser(1L)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
