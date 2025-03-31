package com.university.jsf;

import com.university.entity.Club;
import com.university.entity.Student;
import com.university.service.ClubService;
import com.university.service.StudentService;
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
public class ClubBean implements Serializable {
    
    @Inject
    private ClubService clubService;
    
    @Inject
    private StudentService studentService;
    
    private List<Club> clubs;
    private Club selectedClub;
    private Club newClub = new Club();
    private boolean useJpa = false;
    
    @PostConstruct
    public void init() {
        loadClubs();
    }
    
    public void loadClubs() {
        if (useJpa) {
            clubs = clubService.getAllClubsJpa();
        } else {
            clubs = clubService.getAllClubsMyBatis();
        }
    }
    
    public void saveClub() {
        if (useJpa) {
            clubService.saveClubJpa(newClub);
        } else {
            clubService.saveClubMyBatis(newClub);
        }
        newClub = new Club();
        loadClubs();
    }
    
    public void selectClub(SelectEvent<Club> event) {
        Club club = event.getObject();
        if (useJpa) {
            this.selectedClub = clubService.getClubByIdJpa(club.getId());
        } else {
            this.selectedClub = clubService.getClubByIdMyBatis(club.getId());
        }
    }
    
    public void toggleDataAccessMethod() {
        useJpa = !useJpa;

        loadClubs();

        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Data Access Changed", 
                "Now using " + getDataAccessType()));
    }

    public List<Club> getClubs() {
        return clubs;
    }
    
    public Club getNewClub() {
        return newClub;
    }
    
    public void setNewClub(Club newClub) {
        this.newClub = newClub;
    }
    
    public boolean isUseJpa() {
        return useJpa;
    }
    
    public void setUseJpa(boolean useJpa) {
        this.useJpa = useJpa;
    }
    
    public Club getSelectedClub() {
        return selectedClub;
    }
    
    public void setSelectedClub(Club selectedClub) {
        this.selectedClub = selectedClub;
    }
    
    public String getDataAccessType() {
        return useJpa ? "JPA/ORM" : "MyBatis";
    }
}
