package com.effectivemobile.taskmanagementsystem.validator;

import com.effectivemobile.taskmanagementsystem.security.RoleEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

    @Override
    public void initialize(ValidRole constraintAnnotation) {
    }

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        if (role == null) {
            return false;
        }

        try {
            RoleEnum.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
