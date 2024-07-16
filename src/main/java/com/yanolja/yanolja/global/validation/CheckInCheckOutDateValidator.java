package com.yanolja.yanolja.global.validation;

import com.yanolja.yanolja.global.validation.type.CheckInCheckOutDateValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckInCheckOutDateValidator implements ConstraintValidator<CheckInCheckOutDateValidation, Object> {

    private String checkInDate;
    private String checkOutDate;

    @Override
    public void initialize(CheckInCheckOutDateValidation constraintAnnotation) {
        checkInDate = constraintAnnotation.checkInDate(); // 애노테이션에 저장된 비교할 필드
        checkOutDate = constraintAnnotation.checkOutDate(); // 애노테이션에 저장된 비교할 필드
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        LocalDate checkInDate = getFieldValue(object, this.checkInDate);
        LocalDate checkOutDate = getFieldValue(object, this.checkOutDate);

        if (!checkInDate.isAfter(LocalDate.now()) && !checkInDate.equals(LocalDate.now()) ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("현재날짜보다 체크인 날짜가 빠를 수 없습니다.")//오류메세지
                .addPropertyNode(this.checkInDate)//대상 필드
                .addConstraintViolation();
            return false;
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("체크인날짜보다 체크아웃 날짜가 빠르거나 같을 수 없습니다.")//오류메세지
                .addPropertyNode(this.checkOutDate)//대상 필드
                .addConstraintViolation();
            return false;
        }
        return true;
    }

    // 리플렉션을 이용하여 필드를 가져옴
    private LocalDate getFieldValue(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        Field dateField;
        try {
            dateField = clazz.getDeclaredField(fieldName);
            dateField.setAccessible(true);
            Object target = dateField.get(object);
            if (!(target instanceof LocalDate)) {
                throw new ClassCastException("casting exception");
            }
            return (LocalDate) target;
        } catch (NoSuchFieldException e) {
            log.error("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
        }
        throw new RuntimeException("server error");
    }
}
