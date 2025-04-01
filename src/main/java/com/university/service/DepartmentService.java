package com.university.service;

import com.university.dao.jpa.DepartmentJpaDao;
import com.university.dao.mybatis.DepartmentMyBatisDao;
import com.university.entity.Department;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class DepartmentService {
    
    @Inject
    private DepartmentJpaDao departmentJpaDao;
    
    @Inject
    private DepartmentMyBatisDao departmentMyBatisDao;
    
    // JPA methods
    public List<Department> getAllDepartmentsJpa() {
        return departmentJpaDao.findAll();
    }
    
    public Department getDepartmentByIdJpa(Long id) {
        return departmentJpaDao.findById(id);
    }
    
    @Transactional
    public void saveDepartmentJpa(Department department) {
        departmentJpaDao.save(department);
    }
    
    @Transactional
    public void addStudentToDepartmentJpa(Long studentId, Long departmentId) {
        departmentJpaDao.addStudentToDepartment(studentId, departmentId);
    }
    
    // MyBatis methods
    public List<Department> getAllDepartmentsMyBatis() {
        return departmentMyBatisDao.findAll();
    }
    
    public Department getDepartmentByIdMyBatis(Long id) {
        return departmentMyBatisDao.findById(id);
    }
    
    @Transactional
    public void saveDepartmentMyBatis(Department department) {
        departmentMyBatisDao.save(department);
    }
    
    @Transactional
    public void addStudentToDepartmentMyBatis(Long studentId, Long departmentId) {
        departmentMyBatisDao.addStudentToDepartment(studentId, departmentId);
    }
}
