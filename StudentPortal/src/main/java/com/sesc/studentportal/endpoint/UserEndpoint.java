package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.model.User;
import com.sesc.studentportal.services.UserService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;

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

    public User registerUser(User user) {
        return userService.createUser(user);
    }
}