package com.sesc.studentportal.controller;

import com.sesc.studentportal.model.User;
import com.sesc.studentportal.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * Controller for the User entity where the Endpoints to access the User entity are defined.
 * The Endpoints are used to perform CRUD operations on the User entity.
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    // Instantiating the UserService with dependency injection.
    private final UserService userService;

    /***
     * Gets the user by the Database ID.
     * @param id the ID of the user to get.
     * @return the User object.
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /***
     * Deletes a user by their id.
     * @param id The id of the user to delete.
     */
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            return "User not found";
        }
        return "User deleted";
    }

    /***
     * Updates a user by their id.
     * @param id The id of the user to update.
     * @param user The user object to update.
     * @return The updated user object.
     */
    @PostMapping("/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    /***
     * Gets all the users in the database.
     * @return A list of all the users.
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /***
     * Registers a new user.
     * @param user the user
     * @return the user object.
     */
    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        if (userService.getUserByUsername(username).isPresent()) {
            return userService.getUserByUsername(username).get();
        } else {
            // TODO: Modify this with custom Exception
            throw new RuntimeException("User not found");
        }
    }

    // CONSTRUCTOR //

    /***
     * Constructor for the UserController.
     * @param userService the UserService.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
