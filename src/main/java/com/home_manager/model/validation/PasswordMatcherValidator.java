package com.home_manager.model.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.yaml.snakeyaml.introspector.Property;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, Object> {

    private String password;
    private String confirmPassword;
    private String message;

    @Override
    public void initialize(PasswordMatcher constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.confirmPassword = constraintAnnotation.confirmPassword();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

        Object password = beanWrapper.getPropertyValue(this.password);
        Object confirmPassword = beanWrapper.getPropertyValue(this.confirmPassword);

        boolean valid;

        if (password == null) {
            valid = confirmPassword == null;
        } else {
            valid = password.equals(confirmPassword);
        }

        if (!valid) {
            context.buildConstraintViolationWithTemplate(this.message).
                    addPropertyNode(this.confirmPassword).
                    addConstraintViolation().
                    disableDefaultConstraintViolation();
        }

        return valid;
    }
}
