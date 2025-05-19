package com.university.cdi;

import com.university.entity.Student;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultStudentFormatter implements StudentFormatter {
    
    @Override
    public String formatStudent(Student student) {
        return String.format("%s %s", student.getFirstName(), student.getLastName());
    }
}
