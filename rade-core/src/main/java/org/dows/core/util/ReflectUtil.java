package org.dows.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {
    /**
     * 判断对象是否为空
     *
     * @param obj
     */
    public static boolean isObjectNull(Object obj){
        if (obj != null) {
            Class<?> objClass = obj.getClass();
            Method[] declaredMethods = objClass.getDeclaredMethods();
            if (declaredMethods.length > 0) {
                int methodCount = 0; // get 方法数量
                int nullValueCount = 0; // 结果为空

                for (Method declaredMethod : declaredMethods) {
                    String name = declaredMethod.getName();
                    if (name.startsWith("get") || name.startsWith("is")){
                        methodCount += 1;
                        try {
                            Object invoke = declaredMethod.invoke(obj);
                            if (invoke == null) {
                                nullValueCount += 1;
                            }
                        } catch (IllegalAccessException | InvocationTargetException e){
                            e.printStackTrace();
                        }
                    }
                }
                return methodCount == nullValueCount;
            }
        }
        return false;
    }
}