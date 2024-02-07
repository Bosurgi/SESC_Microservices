package com.sesc.studentportal.repository;

import com.sesc.studentportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/***
 * UserRepository which allows CRUD operations on the User entity in the database.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /***
     * Find a user by its username
     * @param username the username
     * @return the User
     */
    User findUserByUserName(String username);

    /***
     * Find a user by its email
     * @param email
     * @return the User
     */
    User findUserByEmail(String email);
}
