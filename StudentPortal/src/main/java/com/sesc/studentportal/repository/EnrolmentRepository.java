package com.sesc.studentportal.repository;

import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/***
 * Enrolment Repository which allows CRUD operations on the Enrolment entity in the database.
 */
public interface EnrolmentRepository extends JpaRepository<Enrolments, Long> {

    /***
     * Find an enrolment by its module and student
     * @param module the module to search for
     * @param student the student to search for
     * @return the Enrolment
     */
    Enrolments findEnrolmentsByModuleAndStudent(Module module, Student student);

    /***
     * Find all enrolments by a student
     * @param student the student to search for
     * @return the list of Enrolments for the student
     */
    List<Enrolments> findEnrolmentsByStudent(Student student);
}
