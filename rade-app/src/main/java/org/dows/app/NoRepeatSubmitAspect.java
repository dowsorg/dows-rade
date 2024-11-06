package org.dows.app;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.dows.aac.AacApi;
import org.dows.core.annotation.NoRepeatSubmit;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.lock.RadeLock;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class NoRepeatSubmitAspect {

    private final RadeLock radeLock;

    private final AacApi aacApi;

    @Around("@annotation(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        String key = request.getRequestURI() + ":" + aacApi.getCurrentUserId();
        // 加锁
        RadePreconditions.check(!radeLock.tryLock(key, Duration.ofMillis(noRepeatSubmit.expireTime())), "请勿重复操作");
        try {
            return joinPoint.proceed();
        } finally {
            // 移除锁
            radeLock.unlock(key);
        }
    }
}
