package com.university.jsf;

import com.university.cdi.DefaultStudentFormatter;
import com.university.cdi.Logged;
import com.university.cdi.StudentFormatter;
import com.university.entity.Student;
import com.university.service.StudentService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class CdiDemoBean {

    @Inject
    private StudentService studentService;
    
    @Inject
    private DefaultStudentFormatter studentFormatter;
    
    private List<Student> students;
    private Map<Long, String> formattedStudents = new HashMap<>();
    
    @PostConstruct
    public void init() {
        loadStudents();
    }
    
    @Logged
    public void loadStudents() {
        students = studentService.getAllStudentsJpa();
        formattedStudents = students.stream()
            .collect(Collectors.toMap(
                Student::getId,
                student -> studentFormatter.formatStudent(student)
            ));
    }
    
    public String formatCurrentImplementation() {
        return studentFormatter.getClass().getSimpleName();
    }
    
    // Getters
    public List<Student> getStudents() {
        return students;
    }
    
    public Map<Long, String> getFormattedStudents() {
        return formattedStudents;
    }
}
