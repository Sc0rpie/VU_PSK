package com.university.dao.jpa;

import com.university.entity.Club;
import com.university.entity.Course;
import com.university.entity.Student;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class StudentJpaDao {
    
    @PersistenceContext(unitName = "universityPU")
    private EntityManager em;
    
    public List<Student> findAll() {
        // Set FlushMode to COMMIT to prevent auto-flush during query execution
        Query query = em.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.clubs LEFT JOIN FETCH s.courses", Student.class);
        query.setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }
    
    public Student findById(Long id) {
        // First find the entity without modifying it
        Student student = em.find(Student.class, id);
        
        // If student exists but has null version, fix it with a native query
        // to avoid triggering optimistic lock exception
        if (student != null && student.getVersion() == null) {
            // Use a native query to update the version without triggering optimistic locking
            em.createNativeQuery("UPDATE students SET version = 0 WHERE id = :id AND version IS NULL")
                .setParameter("id", id)
                .executeUpdate();
            
            // Clear the persistence context to force a refresh
            em.clear();
            
            // Re-fetch the entity with the updated version
            student = em.find(Student.class, id);
        }
        
        return student;
    }
    
    @Transactional
    public void save(Student student) {
        // Ensure version is not null
        if (student.getVersion() == null) {
            student.setVersion(0L);
        }
        
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
