package com.university.mybatis;

import com.university.entity.Course;
import org.mybatis.cdi.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> findAll();
    Course findById(Long id);
    void insert(Course course);
}
