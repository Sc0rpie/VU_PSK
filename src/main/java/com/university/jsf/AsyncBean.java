package com.university.jsf;

import com.university.async.LongRunningCalculator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Named
@ViewScoped
public class AsyncBean implements Serializable {
    
    @Inject
    private LongRunningCalculator calculator;
    
    private int complexity = 2;
    private boolean calculationInProgress = false;
    private Future<LongRunningCalculator.CalculationResult> futureResult;
    private List<String> log = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        log.add("Async component initialized");
    }

    public void startCalculation() {
        log.add("Starting calculation with complexity: " + complexity);
        futureResult = calculator.performComplexCalculation(complexity);
        calculationInProgress = true;
        
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Calculation started", "Complexity: " + complexity));
    }

    public void checkStatus() {
        if (futureResult == null) {
            log.add("No calculation in progress");
            return;
        }
        
        if (futureResult.isDone()) {
            log.add("Calculation is complete. Retrieving result...");
            try {
                LongRunningCalculator.CalculationResult result = futureResult.get();
                log.add("Result received: " + result.getResult());
                log.add("Status: " + (result.isSuccess() ? "Success" : "Failed"));
                log.add("Message: " + result.getMessage());
                
                calculationInProgress = false;
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Calculation finished", 
                        "Result: " + result.getResult()));
                
            } catch (InterruptedException | ExecutionException e) {
                log.add("Error retrieving calculation result: " + e.getMessage());
                calculationInProgress = false;
                
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            }
        } else {
            log.add("Calculation still in progress...");
            
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "In progress", "Calculation is still running"));
        }
    }

    public void clearLog() {
        log.clear();
        log.add("Log cleared");
    }

    public void cancelCalculation() {
        if (futureResult != null && !futureResult.isDone()) {
            boolean cancelled = futureResult.cancel(true);
            log.add("Calculation cancellation attempt: " + (cancelled ? "successful" : "failed"));
            
            if (cancelled) {
                calculationInProgress = false;
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Cancelled", "Calculation was cancelled"));
            }
        } else {
            log.add("No active calculation to cancel");
        }
    }

    // Getters and setters
    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public boolean isCalculationInProgress() {
        return calculationInProgress;
    }

    public List<String> getLog() {
        return log;
    }
}
