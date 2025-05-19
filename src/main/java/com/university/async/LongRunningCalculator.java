package com.university.async;

import com.university.cdi.Logged;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

@Stateless
public class LongRunningCalculator implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(LongRunningCalculator.class.getName());
    
    @Logged
    @Asynchronous
    public Future<CalculationResult> performComplexCalculation(int complexity) {
        LOG.info("Starting complex calculation with complexity: " + complexity);
        
        try {
            Thread.sleep(complexity * 1000L);
            
            int result = complexity * 42;
            
            LOG.info("Calculation completed with result: " + result);
            return new AsyncResult<>(new CalculationResult(result, true, "Calculation completed successfully"));
            
        } catch (InterruptedException e) {
            LOG.severe("Calculation interrupted: " + e.getMessage());
            return new AsyncResult<>(new CalculationResult(0, false, "Calculation interrupted: " + e.getMessage()));
        }
    }
    
    public static class CalculationResult implements Serializable {
        private final int result;
        private final boolean success;
        private final String message;
        
        public CalculationResult(int result, boolean success, String message) {
            this.result = result;
            this.success = success;
            this.message = message;
        }
        
        public int getResult() {
            return result;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
