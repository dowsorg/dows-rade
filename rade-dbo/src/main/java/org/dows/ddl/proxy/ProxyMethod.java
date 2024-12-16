package org.dows.ddl.proxy;

import org.dows.ddl.utils.StrUtils;
import org.dows.ddl.utils.StrategyMethodUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ProxyMethod implements InvocationHandler {
    private final Object target;

    private final ReentrantLock lock = new ReentrantLock();

    public ProxyMethod(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        AtomicInteger counter = new AtomicInteger();
        for (Parameter p : method.getParameters()) {
            if (null == args[counter.get()] | StrUtils.blank(args[counter.get()]))
                throw new RuntimeException("not null !!!");

            args[counter.get()] = StrategyMethodUtil.execute(p, args[counter.get()], target);
            counter.getAndIncrement();
        }
        try {
            lock.lock();
            return method.invoke(Object.class.equals(method.getDeclaringClass()) ? this : target, args);
        } finally {
            lock.unlock();
        }
    }
}
