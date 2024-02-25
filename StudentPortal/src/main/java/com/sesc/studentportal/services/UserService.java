package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.StudentRepository;
import com.sesc.studentportal.repository.UserRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/***
 * Service for the User entity where the business logic for the User entity is defined.
 */
@BrowserCallable
@AnonymousAllowed
@Service
public class UserService {

    // Instantiating the UserRepository and StudentRepository with dependency injection.
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    /***
     * Gets all the users in the database.
     * @return A list of all the users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /***
     * Gets the user by the Database ID.
     * @param id the ID of the user to get.
     * @return the User object.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /***
     * Gets the user by the username.
     * @param username the username of the user to get.
     * @return the User object.
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    /***
     * Deletes a user by their id.
     * @param id The id of the user to delete.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /***
     * Updates a user by their id.
     * @param id The id of the user to update.
     * @param user The user object to update.
     * @return The updated user object.
     */
    public User updateUser(Long id, User user) {
        User userToUpdate = userRepository.findById(id).orElse(null);
        if (userToUpdate != null) {
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setRoles(user.getRoles());
            userToUpdate.setEmail(user.getEmail());
//            userToUpdate.setRole(user.getRole());
            userRepository.save(userToUpdate);
        }
        return userToUpdate;
    }

    /***
     * Gets the corresponding Student associated with the User.
     * @param user the User to get the Student from.
     * @return the Student object associated with the User.
     */
    public Student findStudentFromUser(@NotNull User user) {
        return studentRepository.findStudentByStudentId(user.getUserId());
    }

    /***
     * Creates a new User checking if another student is not already registered with the same username.
     * @param user the User to create.
     * @return the User object.
     */
    public User createUser(@NotNull User user) {
        // TODO: Check if the user is already registered
//        if (userRepository.findUserByUsername(user.getUsername()) == null) {
//            userRepository.save(user);
        return userRepository.save(user);
//        } else {
//            // TODO: Throw Exception
//            return null;
//        }
    }

    // CONSTRUCTOR //

    /***
     * Constructor for the UserService.
     * Using AutoWired annotation to inject the UserRepository into the UserService.
     * @param userRepository the Repository for the User
     */
    public UserService(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

}
