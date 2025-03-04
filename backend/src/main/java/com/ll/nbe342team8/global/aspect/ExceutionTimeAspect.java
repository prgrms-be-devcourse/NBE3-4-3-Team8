package com.ll.nbe342team8.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//응답시간 측정시 필요시 사용
@Aspect
@Component
@Slf4j
public class ExceutionTimeAspect {

    /*
    @Around("execution(* com.ll.nbe342team8.domain..*.controller..*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("API {} executed in {} ms", joinPoint.getSignature(), executionTime);

        return proceed;
    }

     */

}
