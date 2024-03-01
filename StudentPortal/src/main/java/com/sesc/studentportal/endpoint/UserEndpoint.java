package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.services.UserService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.exception.EndpointException;

import java.util.List;
import java.util.Optional;

/**
 * Endpoint for the User entity where the Endpoints to access the User entity are defined.
 * The Endpoints can be used internally on the Student Portal by Hilla.
 * The Controllers are needed to communicate with different services using REST API.
 */
@Endpoint
@AnonymousAllowed
public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a User and saves it into the database.
     *
     * @param user the user to be registered
     * @return the registered user
     * @throws EndpointException if the user already exists it throws an exception to the frontend.
     */
    public User registerUser(User user) throws EndpointException {
        try {
            return userService.createUser(user);
        } catch (Exception e) {
            throw new EndpointException("User already exists");
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    public User updateRole(String username, String role) {
        return userService.updateRole(username, role);
    }

    public List<String> getRoles(String username) {
        return userService.getRoles(username);
    }

    public Student getStudentByUser(User user) {
        return userService.findStudentFromUser(user);
    }
}
