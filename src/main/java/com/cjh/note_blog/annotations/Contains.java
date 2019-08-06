package com.cjh.note_blog.annotations;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 参数匹配校验
 *
 * @author ChangJiahong
 * @date 2019/8/6
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {Contains.EnumValidtor.class})
@Documented
public @interface Contains {
    String message() default "参数不在范围内";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] target() default {};

    public class EnumValidtor implements ConstraintValidator<Contains, String> {

        String[] strs; //匹配参数

        @Override
        public void initialize(Contains constraintAnnotation) {
            strs = constraintAnnotation.target();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            if (strs.length > 0) {
                for (String str : strs) {

                    if (str!=null && str.equals(value)){
                        return true;
                    }

                }
            }
            return false;
        }
    }
}
