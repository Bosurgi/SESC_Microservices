package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Enrolments;
import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.model.Student;
import com.sesc.studentportal.repository.EnrolmentRepository;
import org.springframework.stereotype.Service;

@Service
public class EnrolmentsService {

    private final EnrolmentRepository enrolmentRepository;

    public EnrolmentsService(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }

    public Enrolments createEnrolment(Student student, Module module) throws Exception {
        // Checking if the student is already enrolled in the module
        if (enrolmentRepository.findEnrolmentsByModuleAndStudent(module, student) != null) {
            throw new Exception("Student " + student.getStudentId() + " is already enrolled in module " + module.getModuleId());
        }
        return enrolmentRepository.save(new Enrolments(student, module));
    }


}
