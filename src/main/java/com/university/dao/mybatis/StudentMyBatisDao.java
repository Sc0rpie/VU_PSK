package com.university.dao.mybatis;

import com.university.entity.Student;
import com.university.mybatis.StudentMapper;
import org.mybatis.cdi.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class StudentMyBatisDao {
    
    @Inject
    private StudentMapper studentMapper;
    
    public List<Student> findAll() {
        return studentMapper.findAll();
    }
    
    public Student findById(Long id) {
        return studentMapper.findById(id);
    }
    
    @Transactional
    public void save(Student student) {
        studentMapper.insert(student);
    }
    
    @Transactional
    public void addStudentToClub(Long studentId, Long clubId) {
        studentMapper.addStudentToClub(studentId, clubId);
    }
    
    @Transactional
    public void addStudentToCourse(Long studentId, Long courseId) {
        studentMapper.addStudentToCourse(studentId, courseId);
    }
}
