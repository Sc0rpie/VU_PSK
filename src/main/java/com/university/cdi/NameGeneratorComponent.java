package com.university.cdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Date;

@Named
@RequestScoped
public class NameGeneratorComponent implements Serializable {
    
    @Inject
    private RandomizerComponent randomizerComponent;
    
    private final String instanceId = UUID.randomUUID().toString().substring(0, 8);
    private final Date creationTime = new Date();
    
    private final List<String> firstNames = Arrays.asList(
            "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", 
            "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph"
    );
    
    private final List<String> lastNames = Arrays.asList(
            "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", 
            "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris"
    );
    
    @PostConstruct
    public void init() {
        System.out.println("NameGeneratorComponent " + instanceId + " constructed at " + creationTime);
    }
    
    @PreDestroy
    public void aboutToDie() {
        System.out.println("NameGeneratorComponent " + instanceId + " ready to die at " + new Date());
    }
    
    public String generateName() {
        int firstNameIndex = randomizerComponent.getRandomNumber(firstNames.size());
        int lastNameIndex = randomizerComponent.getSavedFirstRandomNumber();
        
        String firstName = firstNames.get(firstNameIndex);
        String lastName = lastNames.get(lastNameIndex);
        
        return firstName + " " + lastName + " (generated at " + new Date() + ", component: " + instanceId + ")";
    }
    
    public String getComponentInfo() {
        return "RequestScoped NameGeneratorComponent: instance=" + instanceId + ", created=" + creationTime;
    }
    
    public String getRandomizerInfo() {
        return randomizerComponent.getInstanceInfo();
    }

    public int getSavedFirstRandomNumber() {
        return randomizerComponent.getSavedFirstRandomNumber();
    }
}
