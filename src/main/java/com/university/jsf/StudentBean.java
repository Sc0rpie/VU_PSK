package com.university.jsf;

import com.university.entity.Club;
import com.university.entity.Course;
import com.university.entity.Student;
import com.university.service.ClubService;
import com.university.service.CourseService;
import com.university.service.StudentService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class StudentBean implements Serializable {
    
    @Inject
    private StudentService studentService;
    
    @Inject
    private ClubService clubService;
    
    @Inject
    private CourseService courseService;
    
    private List<Student> students;
    private List<Club> availableClubs;
    private List<Course> availableCourses;
    private Student newStudent = new Student();
    private Student selectedStudent;
    private Long selectedClubId;
    private Long selectedCourseId;
    private boolean useJpa = false; // Toggle between JPA and MyBatis
    
    @PostConstruct
    public void init() {
        loadStudents();
        loadAvailableClubs();
        loadAvailableCourses();
    }
    
    public void loadStudents() {
        if (useJpa) {
            students = studentService.getAllStudentsJpa();
        } else {
            students = studentService.getAllStudentsMyBatis();
        }
    }
    
    public void loadAvailableClubs() {
        if (useJpa) {
            availableClubs = clubService.getAllClubsJpa();
        } else {
            availableClubs = clubService.getAllClubsMyBatis();
        }
    }
    
    public void loadAvailableCourses() {
        if (useJpa) {
            availableCourses = courseService.getAllCoursesJpa();
        } else {
            availableCourses = courseService.getAllCoursesMyBatis();
        }
    }
    
    public void saveStudent() {
        if (useJpa) {
            studentService.saveStudentJpa(newStudent);
        } else {
            studentService.saveStudentMyBatis(newStudent);
        }
        newStudent = new Student();
        loadStudents();
    }
    
    public void selectStudent(Student student) {
        this.selectedStudent = student;
    }
    
    public void addStudentToClub() {
        if (selectedStudent != null && selectedClubId != null) {
            if (useJpa) {
                studentService.addStudentToClubJpa(selectedStudent.getId(), selectedClubId);
            } else {
                studentService.addStudentToClubMyBatis(selectedStudent.getId(), selectedClubId);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student added to club"));
            
            loadStudents();
            selectedClubId = null;
        }
    }
    
    public void addStudentToCourse() {
        if (selectedStudent != null && selectedCourseId != null) {
            if (useJpa) {
                studentService.addStudentToCourseJpa(selectedStudent.getId(), selectedCourseId);
            } else {
                studentService.addStudentToCourseMyBatis(selectedStudent.getId(), selectedCourseId);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student enrolled in course"));
            
            loadStudents();
            selectedCourseId = null;
        }
    }
    
    public void toggleDataAccessMethod() {
        this.useJpa = !this.useJpa;
        
        loadStudents();
        loadAvailableClubs();
        loadAvailableCourses();
        
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Data Access Changed", 
                "Now using " + getDataAccessType()));
        
        System.out.println("DATA ACCESS TOGGLED - Now using: " + getDataAccessType());
    }
    
    public List<Student> getStudents() {
        return students;
    }
    
    public Student getNewStudent() {
        return newStudent;
    }
    
    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }
    
    public boolean isUseJpa() {
        return useJpa;
    }
    
    public void setUseJpa(boolean useJpa) {
        this.useJpa = useJpa;
    }
    
    public String getDataAccessType() {
        return useJpa ? "JPA/ORM" : "MyBatis";
    }
    
    public Student getSelectedStudent() {
        return selectedStudent;
    }
    
    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
    }
    
    public Long getSelectedClubId() {
        return selectedClubId;
    }
    
    public void setSelectedClubId(Long selectedClubId) {
        this.selectedClubId = selectedClubId;
    }
    
    public Long getSelectedCourseId() {
        return selectedCourseId;
    }
    
    public void setSelectedCourseId(Long selectedCourseId) {
        this.selectedCourseId = selectedCourseId;
    }
    
    public List<Club> getAvailableClubs() {
        return availableClubs;
    }
    
    public List<Course> getAvailableCourses() {
        return availableCourses;
    }
}
