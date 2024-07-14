package com.yanolja.yanolja.global.validation.type;


import com.yanolja.yanolja.global.validation.CategoryValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryValidator.class)
public @interface CategoryValidation {
    String message() default "Invalid category";

    Class[] groups() default {};

    Class[] payload() default {};


}
