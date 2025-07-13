package com.flapkap.vendingmachine.aspect;

import com.flapkap.vendingmachine.web.RestResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Aspect for REST APIs,
 * Log in Request and out Response,
 * Set total execution milliseconds of the API.
 *
 * @author Mahmoud Shtayeh
 */
@Slf4j
@Aspect
@Component
public class RestAspect {
    /**
     * Rest API Http request
     */
    private final HttpServletRequest request;

    /**
     * Inject Http request if exists
     *
     * @param request Rest API Http request
     */
    public RestAspect(@Autowired(required = false) final HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Pointcut that matches all Spring beans in the application's controller package.
     */
    @Pointcut("within(com.flapkap.vendingmachine.controller..*)")
    public void isController() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void isRestController() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all the methods that return an API Response instance,
     * Or any subtype of it
     */
    @Pointcut("execution(public com.flapkap.vendingmachine.web.RestResponse+ *(..))")
    public void returnsRestResponse() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches RestFul APIs that return API Response
     */
    @Pointcut("isController() && isRestController() && returnsRestResponse()")
    public void isRestAPI() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that Specify the API total execution time.
     *
     * @param joinPoint join point for advice.
     * @return API response with execution time specified.
     * @throws Throwable throws any exception out of the API.
     */
    @Around("isRestAPI()")
    public Object specifyTotalExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        final LocalDateTime startTime = LocalDateTime.now();
        if (log.isInfoEnabled()) {
            log.info("Enter: {}-- {} | " +
                            "Method: {}() | " +
                            "Start Time: {} | " +
                            "Argument[s]: [{}]", request.getMethod().toUpperCase(Locale.ENGLISH),
                    request.getRequestURI(), joinPoint.getSignature().getName(), startTime, joinPoint.getArgs());
        }

        final RestResponse<?> apiResponse = (RestResponse<?>) joinPoint.proceed();

        final LocalDateTime endTime = LocalDateTime.now();
        final long executionMillis = Duration.between(startTime, endTime).toMillis();
        apiResponse.setExecutionTime(MessageFormat.format("{0}ms", executionMillis));
        if (log.isInfoEnabled()) {
            log.info("Exit: {}-- {} | " +
                            "Method: {}() | " +
                            "End Time: {} | " +
                            "Response: {} | " +
                            "Total Execution Time: {}ms", request.getMethod().toUpperCase(Locale.ENGLISH),
                    request.getRequestURI(), joinPoint.getSignature().getName(), endTime, apiResponse,
                    Duration.between(startTime, endTime).toMillis());
        }
        return apiResponse;
    }
}