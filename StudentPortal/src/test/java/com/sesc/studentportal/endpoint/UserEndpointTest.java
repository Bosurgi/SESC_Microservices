package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.UserRepository;
import com.sesc.studentportal.services.UserService;
import dev.hilla.exception.EndpointException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserEndpointTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEndpoint userEndpoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() throws Exception {
        // Arrange
        User user = new User("testUser", "password", "ROLE_USER");
        when(userService.createUser(user)).thenReturn(user);

        // Act
        User result = userEndpoint.registerUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testRegisterUserWhenUserExists() throws Exception {
        // Arrange
        User user = new User("testUser", "password", "ROLE_USER");
        when(userService.createUser(user)).thenThrow(new EndpointException("User already exists"));

        // Act & Assert
        assertThrows(EndpointException.class, () -> userEndpoint.registerUser(user));
    }

    @Test
    void testGetUserByUsername() {
        // Arrange
        String username = "testUser";
        User user = new User(username, "password", "ROLE_USER");
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userEndpoint.getUserByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetRoles() {
        // Arrange
        String username = "testUser";
        List<String> roles = Collections.singletonList("ROLE_USER");
        when(userService.getRoles(username)).thenReturn(roles);

        // Act
        List<String> result = userEndpoint.getRoles(username);

        // Assert
        assertEquals(roles, result);
    }

    @Test
    void testGetStudentByUser() {
        // Arrange
        User user = new User("testUser", "password", "ROLE_USER");
        Student student = new Student("Mario", "Rossi");
        when(userService.findStudentFromUser(user)).thenReturn(student);

        // Act
        Student result = userEndpoint.getStudentByUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(student, result);
    }

    @Test
    void testUpdateUserInformation() {
        // Arrange
        User user = new User("testUser", "password", "ROLE_USER");
        user.setUserId(1L);
        when(userService.updateUser(user.getUserId(), user)).thenReturn(user);

        // Act
        User result = userEndpoint.updateUserInformation(user);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }
}