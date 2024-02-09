package com.sesc.studentportal.controller;

import com.sesc.studentportal.model.User;
import com.sesc.studentportal.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

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

    @PostMapping("/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
