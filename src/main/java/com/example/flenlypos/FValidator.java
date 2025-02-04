package com.example.flenlypos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FValidator {

    @Autowired
    Validator validator;

    public  <T> void validateParam(T object) {
        Set<ConstraintViolation<T>> errors = validator.validate(object);
        if (!errors.isEmpty()) throw new ConstraintViolationException(errors);
    }
}
