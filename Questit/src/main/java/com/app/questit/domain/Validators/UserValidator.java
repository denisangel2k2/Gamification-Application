package com.app.questit.domain.Validators;

import com.app.questit.domain.User;
import com.app.questit.utils.exceptions.ValidationException;

public class UserValidator implements Validator<User>{

    /**
     * Validates the user
     * @param entity
     * @throws ValidationException if it is not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String errors="";
        if (entity.getLast_name().length()<3)
            errors+="Last name is too short!\n";
        if (entity.getFirst_name().length()<3)
            errors+="First name is too short!\n";
        if (!entity.getEmail().matches(".{6,}[@].{2,}[.].{2,}"))
            errors+="Email is invalid!\n";

        if (errors.length()>0)
            throw new ValidationException(errors);
    }
}
