package com.university.service;

import com.university.dao.jpa.StudentJpaDao;
import com.university.dao.mybatis.StudentMyBatisDao;
import com.university.entity.Club;
import com.university.entity.Course;
import com.university.entity.Student;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class StudentService {
    
    @Inject
    private StudentJpaDao studentJpaDao;
    
    @Inject
    private StudentMyBatisDao studentMyBatisDao;
    
    // JPA methods
    public List<Student> getAllStudentsJpa() {
        return studentJpaDao.findAll();
    }
    
    public Student getStudentByIdJpa(Long id) {
        return studentJpaDao.findById(id);
    }
    
    @Transactional
    public void saveStudentJpa(Student student) {
        studentJpaDao.save(student);
    }
    
    @Transactional
    public void addStudentToClubJpa(Long studentId, Long clubId) {
        studentJpaDao.addStudentToClub(studentId, clubId);
    }
    
    @Transactional
    public void addStudentToCourseJpa(Long studentId, Long courseId) {
        studentJpaDao.addStudentToCourse(studentId, courseId);
    }
    
    // MyBatis methods
    public List<Student> getAllStudentsMyBatis() {
        return studentMyBatisDao.findAll();
    }
    
    public Student getStudentByIdMyBatis(Long id) {
        return studentMyBatisDao.findById(id);
    }
    
    @Transactional
    public void saveStudentMyBatis(Student student) {
        studentMyBatisDao.save(student);
    }
    
    @Transactional
    public void addStudentToClubMyBatis(Long studentId, Long clubId) {
        studentMyBatisDao.addStudentToClub(studentId, clubId);
    }
    
    @Transactional
    public void addStudentToCourseMyBatis(Long studentId, Long courseId) {
        studentMyBatisDao.addStudentToCourse(studentId, courseId);
    }
}
