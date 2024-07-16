package com.yanolja.yanolja.global.validation;

import com.yanolja.yanolja.domain.accommodation.model.type.Category;
import com.yanolja.yanolja.domain.accommodation.model.type.Region;
import com.yanolja.yanolja.global.validation.type.CategoryValidation;
import com.yanolja.yanolja.global.validation.type.RegionValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegionValidator implements ConstraintValidator<RegionValidation,String> {


    @Override
    public void initialize(RegionValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String region, ConstraintValidatorContext constraintValidatorContext) {
        if (!Region.checkValidCategory(region)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("유효한 카테고리 요청이 아닙니다.")//context에 오류메세지와
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
