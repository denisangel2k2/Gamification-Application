package com.app.questit.domain.Validators;

import com.app.questit.utils.exceptions.ValidationException;

public interface Validator <T> {
    void validate(T entity) throws ValidationException;

}
