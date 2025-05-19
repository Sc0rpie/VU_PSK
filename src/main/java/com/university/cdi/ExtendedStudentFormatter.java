package com.university.cdi;

import com.university.entity.Student;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;

@Alternative
public class ExtendedStudentFormatter extends DefaultStudentFormatter {
    
    @Override
    public String formatStudent(Student student) {
        String baseName = String.format("%s %s", student.getFirstName(), student.getLastName());
//        String baseName = super.formatStudent(student);
        String departmentInfo = (student.getDepartment() != null) 
            ? " - Department: " + student.getDepartment().getName() 
            : " - No Department";
            
        return baseName + departmentInfo;
    }
}
