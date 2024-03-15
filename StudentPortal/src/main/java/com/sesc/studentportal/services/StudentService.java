package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.model.User;
import com.sesc.studentportal.repository.StudentRepository;
import com.sesc.studentportal.repository.UserRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@PermitAll
public class StudentService {

    final public StudentRepository studentRepository;
    final public UserRepository userRepository;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Registers a student to the Student Portal by checking if the username provided is present in the database.
     * Then it proceeds to create a new student and save it to the database linking it to the user.
     *
     * @param username the username of the student
     * @return the student that has been registered
     */
    public Student registerStudent(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);

        Student student = null;
        if (user.isPresent()) {
            User userToRegister = user.get();
            student = new Student(userToRegister.getSurname(), userToRegister.getFirstname());
            userToRegister.setStudent(student);
            student.setIsEnrolled(true);
            studentRepository.save(student);
        }
        return student;
    }

    /**
     * It updates a Student with new information provided
     *
     * @param student the Student to update
     * @return the updated Student
     */
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }
}
