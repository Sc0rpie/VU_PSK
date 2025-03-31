package com.university.service;

import com.university.dao.jpa.CourseJpaDao;
import com.university.dao.mybatis.CourseMyBatisDao;
import com.university.entity.Course;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class CourseService {
    
    @Inject
    private CourseJpaDao courseJpaDao;
    
    @Inject
    private CourseMyBatisDao courseMyBatisDao;
    
    // JPA methods
    public List<Course> getAllCoursesJpa() {
        return courseJpaDao.findAll();
    }
    
    public Course getCourseByIdJpa(Long id) {
        return courseJpaDao.findById(id);
    }
    
    @Transactional
    public void saveCourseJpa(Course course) {
        courseJpaDao.save(course);
    }
    
    // MyBatis methods
    public List<Course> getAllCoursesMyBatis() {
        return courseMyBatisDao.findAll();
    }
    
    public Course getCourseByIdMyBatis(Long id) {
        return courseMyBatisDao.findById(id);
    }
    
    @Transactional
    public void saveCourseMyBatis(Course course) {
        courseMyBatisDao.save(course);
    }
}
