package com.travel.commons.valiadator;

import com.travel.commons.utils.PhoneUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.function.Predicate;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/9
 */
public class CheckMobileValidator implements ConstraintValidator<CheckMobile, String> {


    private boolean require = false;

    @Override
    public void initialize(CheckMobile constraintAnnotation) {
        require = constraintAnnotation.required();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Predicate<String> predicateM = (String value)-> PhoneUtil.checkPhone(value);
        if(require){
            return predicateM.test(s);
        }else {
            Predicate<String> predicate = (String value)-> StringUtils.isEmpty(value);
            boolean result = predicate.test(s);
            if(!result){
                return predicateM.test(s);
            }
            return result;
        }
    }
}
