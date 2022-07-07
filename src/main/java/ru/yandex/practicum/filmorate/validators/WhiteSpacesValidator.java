package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

class WhiteSpacesValidator implements ConstraintValidator<NoWhiteSpaces, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(name)) {
            return true;
        }
        return !name.contains(" ");
    }
}