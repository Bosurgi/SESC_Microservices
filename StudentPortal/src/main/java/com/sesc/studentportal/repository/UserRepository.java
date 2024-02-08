package com.sesc.studentportal.repository;

import com.sesc.studentportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/***
 * UserRepository which allows CRUD operations on the User entity in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /***
     * Find a user by its username
     * @param username the username
     * @return the User
     */
    User findUserByUserName(String username);

    /***
     * Find a user by its email
     * @param email the email
     * @return the User
     */
    User findUserByEmail(String email);
}
