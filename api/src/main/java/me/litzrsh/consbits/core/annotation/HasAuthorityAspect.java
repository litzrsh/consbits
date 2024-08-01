package me.litzrsh.consbits.core.annotation;

import me.litzrsh.consbits.core.exception.RestfulException;
import me.litzrsh.consbits.core.util.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

import static me.litzrsh.consbits.core.CommonConstants.FORBIDDEN_EXCEPTION;

/**
 * 사용자 권한을 확인하는 AOP 어노테이션 처리기 클래스.
 * 메소드 또는 클래스에 @HasAuthority 어노테이션이 붙어 있을 경우, 해당 메소드 실행 전 권한을 확인합니다.
 */
@Component
@Aspect
@Slf4j
public class HasAuthorityAspect {

    /**
     * @param joinPoint    현재 실행 중인 조인 포인트.
     * @param hasAuthority 권한 확인 어노테이션.
     * @return 메소드 실행 결과.
     * @throws Throwable 권한이 없을 경우 예외를 던집니다.
     * @HasAuthority 어노테이션이 메소드에 붙어 있는 경우 실행 전 권한을 확인합니다.
     */
    @Around("@annotation(hasAuthority)")
    public Object hasAuthorityAnnotation(ProceedingJoinPoint joinPoint, HasAuthority hasAuthority) throws Throwable {
        this.hasAuthority(hasAuthority);
        return joinPoint.proceed();
    }

    /**
     * @param joinPoint 현재 실행 중인 조인 포인트.
     * @return 메소드 실행 결과.
     * @throws Throwable 권한이 없을 경우 예외를 던집니다.
     * @HasAuthority 어노테이션이 클래스에 붙어 있는 경우 실행 전 권한을 확인합니다.
     */
    @Around("within(@me.litzrsh.consbits.core.annotation.HasAuthority *)")
    public Object hasAuthorityClassAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        HasAuthority hasAuthority = method.getDeclaringClass().getAnnotation(HasAuthority.class);
        if (hasAuthority != null) {
            this.hasAuthority(hasAuthority);
        }
        return joinPoint.proceed();
    }

    /**
     * 사용자의 권한을 확인합니다.
     *
     * @param hasAuthority 확인할 권한이 담긴 어노테이션.
     * @throws RestfulException 사용자가 권한을 가지고 있지 않은 경우 예외를 던집니다.
     */
    protected final void hasAuthority(HasAuthority hasAuthority) throws RestfulException {
        log.info("Challenge Authority : {}", List.of(hasAuthority.value()));
        String[] values = hasAuthority.value();
        for (String value : values) {
            if (SessionUtils.hasAuthority(value)) {
                return;
            }
        }
        throw new RestfulException(FORBIDDEN_EXCEPTION);
    }
}
