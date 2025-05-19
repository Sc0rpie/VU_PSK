package com.university.cdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@ApplicationScoped
public class RandomizerComponent implements Serializable {
    
    private final String instanceId = UUID.randomUUID().toString().substring(0, 8);
    private final Date creationTime = new Date();
    private final Random random = new Random();

    private final int savedFirstRandomNumber = getRandomNumber(15);
    
    @PostConstruct
    public void init() {
        System.out.println("RandomizerService " + instanceId + " constructed at " + creationTime);
    }
    
    @PreDestroy
    public void aboutToDie() {
        System.out.println("RandomizerService " + instanceId + " ready to die at " + new Date());
    }
    
    public int getRandomNumber(int max) {
        return random.nextInt(max);
    }
    
    public String getInstanceInfo() {
        return "SessionScope RandomizerService: instance=" + instanceId + ", created=" + creationTime;
    }

    public int getSavedFirstRandomNumber() {
        return savedFirstRandomNumber;
    }
}
