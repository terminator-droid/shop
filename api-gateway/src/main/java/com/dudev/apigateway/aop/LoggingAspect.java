package com.dudev.apigateway.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP‑аспект, логирующий вызовы всех методов сервисного уровня
 * (пакет {@code com.dudev.apigateway.service.impl}).
 *
 * <p>Записывает в лог уровень {@code DEBUG} начало и окончание выполнения
 * метода, что упрощает трассировку запросов через API‑gateway.
 *
 * @author ussdanil
 * @since 1.0.0
 */

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.dudev.apigateway.service.impl.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.debug("START invocation method={}", methodName);
        Object result;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            log.debug("END invocation method={}", methodName);
        }
    }
}
