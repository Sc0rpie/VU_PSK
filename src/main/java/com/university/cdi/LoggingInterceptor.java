package com.university.cdi;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.logging.Logger;

@Logged
@Interceptor
public class LoggingInterceptor implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(LoggingInterceptor.class.getName());
    
    @AroundInvoke
    public Object logMethodCall(InvocationContext context) throws Exception {
        LOGGER.info("Entering method: " + context.getMethod().getName());
        
        long startTime = System.currentTimeMillis();
        try {
            return context.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Exiting method: " + context.getMethod().getName() + 
                      " - execution time: " + (endTime - startTime) + "ms");
        }
    }
}
