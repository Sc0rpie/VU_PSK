package com.university.dao.jpa;

import com.university.entity.Club;
import com.university.entity.Course;
import com.university.entity.Student;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class StudentJpaDao {
    
    @PersistenceContext(unitName = "universityPU")
    private EntityManager em;
    
    public List<Student> findAll() {
        return em.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.clubs LEFT JOIN FETCH s.courses", Student.class).getResultList();
    }
    
    public Student findById(Long id) {
        return em.find(Student.class, id);
    }
    
    @Transactional
    public void save(Student student) {
        if (student.getId() == null) {
            em.persist(student);
        } else {
            em.merge(student);
        }
    }
    
    @Transactional
    public void addStudentToClub(Long studentId, Long clubId) {
        Student student = em.find(Student.class, studentId);
        Club club = em.find(Club.class, clubId);
        
        if (student != null && club != null) {
            student.getClubs().add(club);
            club.getMembers().add(student);
            em.merge(student);
        }
    }
    
    @Transactional
    public void addStudentToCourse(Long studentId, Long courseId) {
        Student student = em.find(Student.class, studentId);
        Course course = em.find(Course.class, courseId);
        
        if (student != null && course != null) {
            student.getCourses().add(course);
            course.getStudents().add(student);
            em.merge(student);
        }
    }
}
