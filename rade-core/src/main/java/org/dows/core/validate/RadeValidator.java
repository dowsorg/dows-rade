package org.dows.core.validate;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.annotation.Validators;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Data
@Component
@Scope("prototype")
public class RadeValidator implements ConstraintValidator<Validators,String> {
    // 加载数据库中规则校验规则在isValid中校验
    //private final ValidatorProvider validatorProvider;

    @PostConstruct
    public  void init(){
        //validatorProvider.loadRule();
    }
    // 初始化，注解标注的信息
    @Override
    public void initialize(Validators constraintAnnotation) {
        // 初始化，采用postConstruct一次
        String[] baned = constraintAnnotation.baned();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
