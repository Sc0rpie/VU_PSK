package com.university.mybatis;

import com.university.entity.Department;
import org.apache.ibatis.annotations.Param;
import org.mybatis.cdi.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<Department> findAll();
    Department findById(Long id);
    void insert(Department department);
    void addStudentToDepartment(@Param("studentId") Long studentId, @Param("departmentId") Long departmentId);
}
