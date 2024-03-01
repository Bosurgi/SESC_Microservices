package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.StudentRepository;
import com.sesc.studentportal.repository.UserRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
     * It also updates the corresponding Student if needed.
     * @param id The id of the user to update.
     * @param user The user object to update.
     * @return The updated user object.
     */
    public User updateUser(Long id, User user) {
        User userToUpdate = userRepository.findById(id).orElse(null);
        Student student = user.getStudent();
        if (userToUpdate != null) {
//            Username shouldn't be changed but if it needs to be changed, uncomment the line below
//            userToUpdate.setUsername(user.getUsername());

//            For future implementation of password change uncomment below
//            userToUpdate.setPassword(user.getPassword());

            userToUpdate.setRoles(user.getRoles());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setFirstname(user.getFirstname());
            userToUpdate.setSurname(user.getSurname());
            student.setFirstName(user.getFirstname());
            student.setSurname(user.getSurname());
            userRepository.save(userToUpdate);
            studentRepository.save(student);
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

    /***
     * Updates the role of a user.
     * @param username the username in the database
     * @param role the role to update
     * @return the updated User object.
     */
    public User updateRole(String username, String role) {
        User userToUpdate = userRepository.findUserByUsername(username).orElse(null);
        String currentRole = userToUpdate.getRoles();
        userToUpdate.setRoles(currentRole + "," + role);
        return userRepository.saveAndFlush(userToUpdate);
    }

    public List<String> getRoles(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not exists by Username")
        );
        return List.of(user.getRoles().split(","));
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
