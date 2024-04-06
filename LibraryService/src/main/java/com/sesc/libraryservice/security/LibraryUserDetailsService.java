package com.sesc.libraryservice.security;

import com.sesc.libraryservice.model.Student;
import com.sesc.libraryservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class LibraryUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Autowired
    public LibraryUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Load user by studentId from the database
     *
     * @param studentId the studentId of the user
     * @return the UserDetails object
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        Student user = studentRepository.findStudentByStudentId(studentId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(user.getStudentId(), user.getPassword(), getAuthorities(user.getRole()));
    }

    /**
     * It maps the Roles from the Student to the GrantedAuthority objects.
     *
     * @param roles the roles of the user
     * @return the Collection of GrantedAuthority objects
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String roles) {
        List<String> roleList = Arrays.asList(roles.split(","));
        return roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
