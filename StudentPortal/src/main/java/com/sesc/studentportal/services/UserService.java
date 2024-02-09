package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.StudentRepository;
import com.sesc.studentportal.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUserName(username);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User user) {
        User userToUpdate = userRepository.findById(id).orElse(null);
        if (userToUpdate != null) {
            userToUpdate.setUserName(user.getUserName());
            userToUpdate.setPassword(user.getPassword());
            userRepository.save(userToUpdate);
        }
        return userToUpdate;
    }

    public Student findStudentFromUser(@NotNull User user) {
        return studentRepository.findStudentByStudentId(user.getUserId());
    }

    public User createUser(@NotNull User user) {
        if (userRepository.findUserByUserName(user.getUserName()) == null) {
            userRepository.save(user);
            return user;
        } else {
            // TODO: Throw Exception
            return null;
        }
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
