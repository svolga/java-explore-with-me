package ru.practicum.ewm.utils.formatter;

import static ru.practicum.ewm.utils.Constant.DATE_TIME_FORMAT;

public class DateTimeFormatter {
    public static final java.time.format.DateTimeFormatter DATE_TIME_FORMATTER =
            java.time.format.DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
}
