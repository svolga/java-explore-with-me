package ru.practicum.ewm.stats.utils;

import java.time.format.DateTimeFormatter;

public class Constant {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    public static final String CONTROLLER_PATH = "ru.practicum.ewm.stats.controller";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String INVALID_DATE_RANGE = "Неверный порядок следования дат";
}
