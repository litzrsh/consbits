package me.litzrsh.consbits.core.annotation;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

/**
 * 재시도 로직을 처리하는 AOP 어노테이션 처리기 클래스.
 * 메소드에 @Retry 어노테이션이 붙어 있을 경우, 지정된 횟수만큼 재시도합니다.
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
@Aspect
@Slf4j
public class RetryAspect {

    /**
     * @Retry 어노테이션이 붙어 있는 메소드를 감싸서 실행 실패 시 지정된 횟수만큼 재시도합니다.
     *
     * @param joinPoint 현재 실행 중인 조인 포인트.
     * @param retry 재시도 설정이 담긴 어노테이션.
     * @return 메소드 실행 결과.
     * @throws Throwable 최대 재시도 횟수만큼 시도한 후에도 실패할 경우 예외를 던집니다.
     */
    @Around("@annotation(retry)")
    public Object retryAnnotation(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        Exception exceptionHolder = null;
        for (int i = 0; i < retry.maxRetries(); i++) {
            if (i > 0) log.info("Retry : {}", i);
            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
                Thread.sleep(retry.retryDelay());
            }
        }
        throw exceptionHolder;
    }
}
