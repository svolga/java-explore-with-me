package ru.practicum.ewm.utils.formatter;

import org.springframework.http.HttpStatus;

public class HttpStatusFormatter {
    public static String formatHttpStatus(HttpStatus status) {
        return status.getReasonPhrase().toUpperCase().replace(" ", "_");
    }
}
