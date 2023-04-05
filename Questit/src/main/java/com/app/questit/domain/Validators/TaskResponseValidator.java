package com.app.questit.domain.Validators;

import com.app.questit.domain.TaskResponse;
import com.app.questit.utils.exceptions.ValidationException;

public class TaskResponseValidator implements Validator<TaskResponse>{
    @Override
    public void validate(TaskResponse entity) throws ValidationException {
       if(entity.getResponse().equals(""))
                throw new ValidationException("Response cannot be empty");
    }
}
