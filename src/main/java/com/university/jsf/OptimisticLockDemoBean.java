package com.university.jsf;

import com.university.entity.Student;
import com.university.service.StudentService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class OptimisticLockDemoBean implements Serializable {
    
    @Inject
    private StudentService studentService;
    
    @PersistenceContext(unitName = "universityPU")
    private EntityManager em;
    
    private List<Student> students;
    private Student selectedStudent;
    private Student staleStudentCopy;
    private String firstNameEdit;
    private String lastNameEdit;
    private boolean showConcurrentForm = false;
    private String operationResult = "";
    private Long originalVersion;
    
    // New fields for conflict resolution
    private boolean showConflictResolution = false;
    private Student freshStudent;
    private String pendingLastNameEdit;
    
    @PostConstruct
    public void init() {
        students = studentService.getAllStudentsJpa();
    }
    
    public void selectStudent(Student student) {
        try {
            this.selectedStudent = studentService.getStudentByIdJpa(student.getId());
            
            this.staleStudentCopy = new Student();
            this.staleStudentCopy.setId(this.selectedStudent.getId());
            this.staleStudentCopy.setFirstName(this.selectedStudent.getFirstName());
            this.staleStudentCopy.setLastName(this.selectedStudent.getLastName());
            this.staleStudentCopy.setEmail(this.selectedStudent.getEmail());
            this.staleStudentCopy.setVersion(this.selectedStudent.getVersion());
            
            this.originalVersion = this.selectedStudent.getVersion();
            
            this.firstNameEdit = selectedStudent.getFirstName();
            this.lastNameEdit = selectedStudent.getLastName();
            this.showConcurrentForm = true;
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Student Selected", 
                    "Selected student with version: " + this.originalVersion));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load student: " + e.getMessage()));
        }
    }
    
    @Transactional
    public void updateFirstUser() {
        try {
            if (selectedStudent != null) {
                selectedStudent.setFirstName(firstNameEdit);
                studentService.saveStudentJpa(selectedStudent);
                
                selectedStudent = studentService.getStudentByIdJpa(selectedStudent.getId());
                
                Long newVersion = selectedStudent.getVersion();
                operationResult = "First user update successful! Version changed from " + 
                                  originalVersion + " to " + newVersion;
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", operationResult));

                students = studentService.getAllStudentsJpa();
            }
        } catch (Exception e) {
            operationResult = "First user error: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", operationResult));
        }
    }
    
    public void updateSecondUser() {
        try {
            if (staleStudentCopy != null) {
                staleStudentCopy.setLastName(lastNameEdit);
                
                studentService.saveStudentJpa(staleStudentCopy);
                
                operationResult = "Second user update completed without optimistic lock exception. This is unexpected!";
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", operationResult));
            }
        } catch (OptimisticLockException e) {
            handleOptimisticLockException(e, "second user");
        } catch (javax.ejb.EJBException e) {
            Throwable cause = e.getCause();
            if (cause instanceof OptimisticLockException) {
                handleOptimisticLockException((OptimisticLockException)cause, "second user");
            } else if (cause instanceof javax.persistence.PersistenceException && 
                       cause.getCause() != null && 
                       cause.getCause().getMessage() != null &&
                       cause.getCause().getMessage().contains("updated or deleted by another transaction")) {
                handleOptimisticLockException(new OptimisticLockException("Detected optimistic lock error", cause), "second user");
            } else {
                operationResult = "Second user error (EJB): " + e.getMessage();
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", operationResult));
            }
        } catch (Exception e) {
            operationResult = "Second user error: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", operationResult));
        } finally {
            students = studentService.getAllStudentsJpa();
        }
    }
    
    private void handleOptimisticLockException(OptimisticLockException e, String user) {
        operationResult = "✓ Optimistic Lock Exception detected! " +
            "The first user changed the entity from version " + originalVersion + 
            " to version " + selectedStudent.getVersion() + ".";
        
        try {
            // Get the fresh version from the database
            freshStudent = studentService.getStudentByIdJpa(selectedStudent.getId());
            
            // Store the pending changes
            pendingLastNameEdit = lastNameEdit;
            
            // Show conflict resolution UI
            showConflictResolution = true;
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Data Conflict Detected", 
                    "The data was modified by another user. Please review the changes and decide if you still want to apply your update."));
            
        } catch (Exception ex) {
            operationResult += "\n\nError retrieving updated data: " + ex.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", operationResult));
        }
    }

    public void applyPendingChanges() {
        try {
            freshStudent.setLastName(pendingLastNameEdit);

            studentService.saveStudentJpa(freshStudent);
            
            operationResult = "Your changes have been applied successfully to the latest version.";
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", operationResult));

            selectedStudent = freshStudent;
            lastNameEdit = freshStudent.getLastName();
            
            // Reset conflict resolution state
            showConflictResolution = false;
            
            // Refresh the student list
            students = studentService.getAllStudentsJpa();
            
        } catch (Exception ex) {
            operationResult = "Error applying your changes: " + ex.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", operationResult));
        }
    }
    
    public void discardPendingChanges() {
        selectedStudent = freshStudent;
        lastNameEdit = freshStudent.getLastName();
        
        operationResult = "Your changes have been discarded.";
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Changes Discarded", operationResult));
        
        // Reset conflict resolution state
        showConflictResolution = false;
    }
    
    public void resetDemo() {
        showConcurrentForm = false;
        showConflictResolution = false;
        selectedStudent = null;
        staleStudentCopy = null;
        freshStudent = null;
        firstNameEdit = null;
        lastNameEdit = null;
        pendingLastNameEdit = null;
        operationResult = "";
        students = studentService.getAllStudentsJpa();
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public String getFirstNameEdit() {
        return firstNameEdit;
    }

    public void setFirstNameEdit(String firstNameEdit) {
        this.firstNameEdit = firstNameEdit;
    }

    public String getLastNameEdit() {
        return lastNameEdit;
    }

    public void setLastNameEdit(String lastNameEdit) {
        this.lastNameEdit = lastNameEdit;
    }

    public boolean isShowConcurrentForm() {
        return showConcurrentForm;
    }

    public void setShowConcurrentForm(boolean showConcurrentForm) {
        this.showConcurrentForm = showConcurrentForm;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    // New getters and setters
    public boolean isShowConflictResolution() {
        return showConflictResolution;
    }
    
    public void setShowConflictResolution(boolean showConflictResolution) {
        this.showConflictResolution = showConflictResolution;
    }
    
    public Student getFreshStudent() {
        return freshStudent;
    }
    
    public String getPendingLastNameEdit() {
        return pendingLastNameEdit;
    }

    public Student getStaleStudentCopy(){return staleStudentCopy; }
}
