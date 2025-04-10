package com.groommoa.aether_back_notification.global.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;

    @Override
    public void initialize(ValidEnum annotation) {
        this.enumClass = annotation.enumClass();
        this.ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // @NotNull로 검증
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .anyMatch(e -> ignoreCase ? e.equalsIgnoreCase(value) : e.equals(value));
    }
}
