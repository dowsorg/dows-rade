package org.dows.core.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.dows.core.validate.RadeValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
// 指定校验器
@Constraint(validatedBy ={RadeValidator.class})
@Target( {METHOD,FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface Validators {

    String[]baned()default{};//不允许你写的用户名。admiin
    String message() default "{jakarta.validation.constrraints.NotEmpty.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
