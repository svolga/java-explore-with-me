package ru.practicum.ewm.utils.validation;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.utils.errors.exceptions.NotAllowedException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

import ru.practicum.ewm.utils.errors.ErrorConstants;

@Slf4j
@UtilityClass
public class EventTimeValidator {

    public static void checkStartTimeIsValid(LocalDateTime time) {
        if (time.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new NotAllowedException(ErrorConstants.TIME_IS_LESS_THAN_TWO_HOUR_BEFORE_START);
        }
    }

    public static void checkStartTimeIsAfterEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException(ErrorConstants.START_AFTER_END);
        }
    }
}
