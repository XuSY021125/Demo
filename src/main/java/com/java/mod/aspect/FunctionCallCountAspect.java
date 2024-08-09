package com.java.mod.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
@Slf4j
public class FunctionCallCountAspect {

    private static final Map<String, AtomicInteger> functionCallCounts = new ConcurrentHashMap<>();

    /**
     * 环绕通知方法，用于在目标方法执行前后执行额外的操作。
     * 此方法会在匹配的方法执行前增加功能调用计数，并在日志中记录当前的功能调用计数。
     *
     * @param joinPoint 代表正在执行的方法调用
     * @return 方法执行的结果
     * @throws Throwable 如果方法执行过程中抛出异常
     */
    @Around("execution(* com.java.mod.controller.*.*(..))")
    public Object countFunctionCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String functionName = method.getName();
        AtomicInteger callCount = functionCallCounts.computeIfAbsent(functionName, k -> new AtomicInteger(0));
        callCount.incrementAndGet(); // 功能调用计数器递增

        log.info("{} 方法被调用 {} 次.", functionName, callCount.get());
        return joinPoint.proceed(); // 继续执行原方法
    }
}

