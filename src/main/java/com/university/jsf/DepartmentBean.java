package com.university.jsf;

import com.university.entity.Department;
import com.university.service.DepartmentService;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class DepartmentBean implements Serializable {
    
    @Inject
    private DepartmentService departmentService;
    
    private List<Department> departments;
    private Department selectedDepartment;
    private Department newDepartment = new Department();
    private boolean useJpa = false;
    
    @PostConstruct
    public void init() {
        loadDepartments();
    }
    
    public void loadDepartments() {
        if (useJpa) {
            departments = departmentService.getAllDepartmentsJpa();
        } else {
            departments = departmentService.getAllDepartmentsMyBatis();
        }
    }
    
    public void saveDepartment() {
        if (useJpa) {
            departmentService.saveDepartmentJpa(newDepartment);
        } else {
            departmentService.saveDepartmentMyBatis(newDepartment);
        }
        newDepartment = new Department();
        loadDepartments();
    }
    
    public void selectDepartment(SelectEvent<Department> event) {
        Department department = event.getObject();
        if (useJpa) {
            this.selectedDepartment = departmentService.getDepartmentByIdJpa(department.getId());
        } else {
            this.selectedDepartment = departmentService.getDepartmentByIdMyBatis(department.getId());
        }
    }
    
    public void toggleDataAccessMethod() {
        useJpa = !useJpa;
        
        loadDepartments();
        
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Data Access Changed", 
                "Now using " + getDataAccessType()));
    }
    
    // Getters and setters
    public List<Department> getDepartments() {
        return departments;
    }
    
    public Department getNewDepartment() {
        return newDepartment;
    }
    
    public void setNewDepartment(Department newDepartment) {
        this.newDepartment = newDepartment;
    }
    
    public boolean isUseJpa() {
        return useJpa;
    }
    
    public void setUseJpa(boolean useJpa) {
        this.useJpa = useJpa;
    }
    
    public Department getSelectedDepartment() {
        return selectedDepartment;
    }
    
    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }
    
    public String getDataAccessType() {
        return useJpa ? "JPA/ORM" : "MyBatis";
    }
}
