package com.sesc.studentportal.repository;

import com.sesc.studentportal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/***
 * ModuleRepository which allows CRUD operations on the Student entity in the database.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /***
     * Find a student by its id
     * @param id the student ID
     * @return the Student
     */
    Student findStudentByStudentId(Long id);

    /***
     * Find a student by its name
     * @param name the student name
     * @return the Student
     */
    Student findStudentByFirstName(String name);

    /***
     * Find a student by its surname
     * @param surname the student surname
     * @return the Student
     */
    Student findStudentBySurname(String surname);

    /***
     * Find a student by its student number
     * @param studentNumber the student number
     * @return the Student
     */
    Student findStudentByStudentNumber(String studentNumber);

    /***
     * Query the Database to search for a student by a keyword
     * @param keyword the keyword to search for
     * @return the Student corresponding to the search
     */
    @Query("SELECT s FROM Student s WHERE CONCAT(s.firstName, s.surname, s.studentId) LIKE %?1%")
    public Student search(String keyword);
}
