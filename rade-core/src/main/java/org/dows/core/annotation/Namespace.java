package org.dows.core.annotation;

import java.lang.annotation.*;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 3/19/2024 7:17 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Namespace {

    String module() default "";

    String name();

    String code();

    String path();

    Class<?> parent() default Object.class;


}
