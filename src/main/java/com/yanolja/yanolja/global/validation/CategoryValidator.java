package com.yanolja.yanolja.global.validation;

import com.yanolja.yanolja.domain.accommodation.model.type.Category;
import com.yanolja.yanolja.global.validation.type.CategoryValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<CategoryValidation, String> {

    @Override
    public void initialize(CategoryValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String category, ConstraintValidatorContext constraintValidatorContext) {
        if (!Category.checkValidCategory(category)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("유효한 카테고리 요청이 아닙니다.")//context에 오류메세지와
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
