package com.university.jsf;

import com.university.entity.Course;
import com.university.service.CourseService;
import org.primefaces.event.SelectEvent;

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
public class CourseBean implements Serializable {
    
    @Inject
    private CourseService courseService;
    
    private List<Course> courses;
    private Course selectedCourse;
    private Course newCourse = new Course();
    private boolean useJpa = false;
    
    @PostConstruct
    public void init() {
        loadCourses();
    }
    
    public void loadCourses() {
        if (useJpa) {
            courses = courseService.getAllCoursesJpa();
        } else {
            courses = courseService.getAllCoursesMyBatis();
        }
    }
    
    public void saveCourse() {
        if (useJpa) {
            courseService.saveCourseJpa(newCourse);
        } else {
            courseService.saveCourseMyBatis(newCourse);
        }
        newCourse = new Course();
        loadCourses();
    }
    
    public void selectCourse(SelectEvent<Course> event) {
        Course course = event.getObject();
        if (useJpa) {
            this.selectedCourse = courseService.getCourseByIdJpa(course.getId());
        } else {
            this.selectedCourse = courseService.getCourseByIdMyBatis(course.getId());
        }
    }
    
    public void toggleDataAccessMethod() {
        useJpa = !useJpa;

        loadCourses();

        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Data Access Changed", 
                "Now using " + getDataAccessType()));
    }

    public List<Course> getCourses() {
        return courses;
    }
    
    public Course getNewCourse() {
        return newCourse;
    }
    
    public void setNewCourse(Course newCourse) {
        this.newCourse = newCourse;
    }
    
    public boolean isUseJpa() {
        return useJpa;
    }
    
    public void setUseJpa(boolean useJpa) {
        this.useJpa = useJpa;
    }
    
    public Course getSelectedCourse() {
        return selectedCourse;
    }
    
    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }
    
    public String getDataAccessType() {
        return useJpa ? "JPA/ORM" : "MyBatis";
    }
}
