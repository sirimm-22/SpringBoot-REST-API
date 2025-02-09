package com.guidewire.simm.usermanagement.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.simm.usermanagement.model.User;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    private static final String FILE_PATH = "src/main/resources/users.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<User> readUsersFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error reading users file", e);
        }
    }

    private void writeUsersToFile(List<User> users) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), users);
        } catch (IOException e) {
            throw new RuntimeException("Error writing users file", e);
        }
    }

    public List<User> getAllUsers() {
        return readUsersFromFile();
    }

    public Optional<User> getUserById(Long id) {
        return readUsersFromFile().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public User createUser(User user) {
        List<User> users = readUsersFromFile();
        user.setId((long) (users.size() + 1));
        users.add(user);
        writeUsersToFile(users);
        return user;
    }

    public User updateUser(Long id, User updatedUser) {
        List<User> users = readUsersFromFile();
        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setName(updatedUser.getName());
                user.setEmail(updatedUser.getEmail());
                user.setAge(updatedUser.getAge());
                writeUsersToFile(users);
                return user;
            }
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        List<User> users = readUsersFromFile();
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) writeUsersToFile(users);
        return removed;
    }
}
