package com.university.cdi;

import com.university.entity.Student;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public abstract class StudentFormatterDecorator implements StudentFormatter {
    @Inject
    @Delegate
    private StudentFormatter delegate;
    
    @Override
    public String formatStudent(Student student) {
        String formattedName = delegate.formatStudent(student);
        return formattedName + " (ID: " + student.getId() + ")";
    }
}
