package com.guidewire.simm.usermanagement.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.simm.usermanagement.model.User;
import com.guidewire.simm.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    private List<User> users;

    @BeforeEach
    void setUp() throws IOException {
        // Load users from users.json
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("users.json");
        users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});

        // Mock service behavior
        when(userService.getAllUsers()).thenReturn(users);
        when(userService.getUserById(1L)).thenReturn(Optional.of(users.get(0)));
        when(userService.createUser(any(User.class))).thenAnswer(invocation -> {
            User newUser = invocation.getArgument(0);
            newUser.setId(99L);
            return newUser;
        });
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(new User(1L, "Updated Name", "updated@example.com", 30));
        when(userService.deleteUser(1L)).thenReturn(true);
    }

    @Test
    void testGetAllUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);

        System.out.println("Response: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body is null");
        assertEquals(6, response.getBody().length, "Expected 8 users, but got " + response.getBody().length);
    }


    @Test
    void testGetUserById() {
        ResponseEntity<User> response = restTemplate.getForEntity("/users/1", User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Dev", response.getBody().getName());
    }

    @Test
    void testCreateUser() {
        User newUser = new User(null, "Charlie", "charlie@example.com", 40);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(newUser, headers);

        ResponseEntity<User> response = restTemplate.postForEntity("/users", request, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Controller returns User, not ResponseEntity
        assertNotNull(response.getBody());
        assertEquals("Charlie", response.getBody().getName());
        assertEquals(99L, response.getBody().getId());
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User(null, "Updated Name", "updated@example.com", 30);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(updatedUser, headers);

        ResponseEntity<User> response = restTemplate.exchange("/users/1", HttpMethod.PUT, request, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    void testDeleteUser() {
        ResponseEntity<Void> response = restTemplate.exchange("/users/1", HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
