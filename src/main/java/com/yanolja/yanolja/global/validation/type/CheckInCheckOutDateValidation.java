package com.yanolja.yanolja.global.validation.type;

import com.yanolja.yanolja.global.validation.CheckInCheckOutDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckInCheckOutDateValidator.class)
public @interface CheckInCheckOutDateValidation {

    String message() default "Bad Request CheckInCheckOutDate";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String checkInDate(); // 대상 객체의 checkInDate 필드 이름을 담을 그릇

    String checkOutDate(); // 대상 객체의 checkInDate 필드 이름을 담을 그릇

}
