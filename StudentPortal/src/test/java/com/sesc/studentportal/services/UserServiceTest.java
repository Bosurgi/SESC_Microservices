package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.StudentRepository;
import com.sesc.studentportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUserWithNullUserObject() {
        // Arrange
        Long id = 1L;

        // Act
        User result = userService.updateUser(id, null);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetUserByUsername() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUsername(username);

        // Assert
        assertEquals(user, result.orElse(null));
    }

    @Test
    void testUpdateUser() {
        // Arrange
        Student student = new Student("USERName", "Usersurname");
        User user = new User("testuser", "password", "ROLE_TEST", "USERName", "Usersurname", "test@gmail.com");
        user.setUserId(1L);
        user.setStudent(student);

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        String newEmail = "newEmail";
        String newRole = "newRole";
        String newName = "newName";
        User updatedUser = new User("testuser", "password", newRole, newName, "Usersurname", newEmail);

        // Act
        User result = userService.updateUser(1L, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals(newEmail, result.getEmail());
    }
}