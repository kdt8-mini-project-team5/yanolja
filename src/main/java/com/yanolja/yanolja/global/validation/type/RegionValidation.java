package com.yanolja.yanolja.global.validation.type;

import com.yanolja.yanolja.global.validation.RegionValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegionValidator.class)
public @interface RegionValidation {
    String message() default "Invalid Region";

    Class[] groups() default {};

    Class[] payload() default {};

}
