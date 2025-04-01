package com.university.dao.jpa;

import com.university.entity.Department;
import com.university.entity.Student;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class DepartmentJpaDao {
    
    @PersistenceContext(unitName = "universityPU")
    private EntityManager em;
    
    public List<Department> findAll() {
        return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
    }
    
    public Department findById(Long id) {
        return em.find(Department.class, id);
    }
    
    @Transactional
    public void save(Department department) {
        if (department.getId() == null) {
            em.persist(department);
        } else {
            em.merge(department);
        }
    }
    
    @Transactional
    public void addStudentToDepartment(Long studentId, Long departmentId) {
        Student student = em.find(Student.class, studentId);
        Department department = em.find(Department.class, departmentId);
        
        if (student != null && department != null) {
            student.setDepartment(department);
            department.getStudents().add(student);
            em.merge(student);
        }
    }
}
