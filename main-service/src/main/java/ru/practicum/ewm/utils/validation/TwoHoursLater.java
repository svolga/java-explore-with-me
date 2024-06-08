package ru.practicum.ewm.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UpdateEventTimeConstraintValidator.class)
public @interface TwoHoursLater {
    String message() default "{Значение даты должно быть в прошлом}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
