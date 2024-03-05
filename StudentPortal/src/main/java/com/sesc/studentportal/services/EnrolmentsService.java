package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.repository.EnrolmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrolmentsService {

    private final EnrolmentRepository enrolmentRepository;

    public EnrolmentsService(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }

    /**
     * It enrols a student to a module and creates the entry in the database.
     *
     * @param student the student to enrol
     * @param module  the module the student is enrolling to
     * @return the Enrolment
     * @throws Exception if the student is already enrolled in the module
     */
    public Enrolments createEnrolment(Student student, Module module) throws Exception {
        // Checking if the student is already enrolled in the module
        if (enrolmentRepository.findEnrolmentsByModuleAndStudent(module, student) != null) {
            // TODO: Create custom Exception
            throw new Exception("Student " + student.getStudentId() + " is already enrolled in module " + module.getModuleId());
        }
        return enrolmentRepository.save(new Enrolments(student, module));
    }

    /**
     * It gets all the enrolments belonging to a student
     *
     * @param student the student to look for
     * @return a list of enrolments
     */
    public List<Enrolments> getAllEnrolmentsFromStudent(Student student) {
        return enrolmentRepository.findEnrolmentsByStudent(student);
    }

    public List<Module> getModulesFromEnrolments(Student student) {
        List<Enrolments> enrolments = enrolmentRepository.findEnrolmentsByStudent(student);
        return enrolments.stream().map(Enrolments::getModule).toList();
    }

}
