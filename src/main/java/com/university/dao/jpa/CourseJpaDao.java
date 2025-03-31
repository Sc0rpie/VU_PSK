package com.university.dao.jpa;

import com.university.entity.Course;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class CourseJpaDao {
    
    @PersistenceContext(unitName = "universityPU")
    private EntityManager em;
    
    public List<Course> findAll() {
        return em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
    }
    
    public Course findById(Long id) {
        return em.find(Course.class, id);
    }
    
    @Transactional
    public void save(Course course) {
        if (course.getId() == null) {
            em.persist(course);
        } else {
            em.merge(course);
        }
    }
}
