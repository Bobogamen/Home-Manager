package com.home_manager.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class StartPeriodValidator implements ConstraintValidator<StartPeriod, LocalDate> {

    public StartPeriodValidator() {
    }

    @Override
    public void initialize(StartPeriod constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {

        if (date != null) {
            return date.getYear() >= 2018;
        }

        return true;
    }
}
