package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.StudentRepository;
import com.sesc.studentportal.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUserName(username);
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
