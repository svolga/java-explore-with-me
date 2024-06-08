package ru.practicum.ewm.stats.utils.validation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats.utils.Constant;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@UtilityClass
public class DateTimeValidator {
    public static void checkStartTimeIsAfterEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException(Constant.INVALID_DATE_RANGE);
        }
    }
}
