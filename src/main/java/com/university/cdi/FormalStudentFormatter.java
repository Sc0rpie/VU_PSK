package com.university.cdi;

import com.university.entity.Student;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
public class FormalStudentFormatter implements StudentFormatter {
    
    @Override
    public String formatStudent(Student student) {
        return String.format("Student: %s, %s", student.getLastName(), student.getFirstName());
    }
}
