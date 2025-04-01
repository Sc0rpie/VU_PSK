package com.university.dao.mybatis;

import com.university.entity.Department;
import com.university.mybatis.DepartmentMapper;
import org.mybatis.cdi.Transactional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class DepartmentMyBatisDao {
    
    @Inject
    private DepartmentMapper departmentMapper;
    
    public List<Department> findAll() {
        return departmentMapper.findAll();
    }
    
    public Department findById(Long id) {
        return departmentMapper.findById(id);
    }
    
    @Transactional
    public void save(Department department) {
        departmentMapper.insert(department);
    }
    
    @Transactional
    public void addStudentToDepartment(Long studentId, Long departmentId) {
        departmentMapper.addStudentToDepartment(studentId, departmentId);
    }
}
