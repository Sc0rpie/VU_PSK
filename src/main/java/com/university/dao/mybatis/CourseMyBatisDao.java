package com.university.dao.mybatis;

import com.university.entity.Course;
import com.university.mybatis.CourseMapper;
import org.mybatis.cdi.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CourseMyBatisDao {
    
    @Inject
    private CourseMapper courseMapper;
    
    public List<Course> findAll() {
        return courseMapper.findAll();
    }
    
    public Course findById(Long id) {
        return courseMapper.findById(id);
    }
    
    @Transactional
    public void save(Course course) {
        courseMapper.insert(course);
    }
}
