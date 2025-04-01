package com.university.mybatis;

import com.university.entity.Student;
import org.mybatis.cdi.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudentMapper {
    List<Student> findAll();
    Student findById(Long id);
    void insert(Student student);
    void addStudentToClub(@Param("studentId") Long studentId, @Param("clubId") Long clubId);
    void addStudentToCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    void updateDepartment(@Param("studentId") Long studentId, @Param("departmentId") Long departmentId);
}
