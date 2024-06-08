package ru.practicum.ewm.utils.logger;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ListLogger<T extends Iterable<?>> {
    public static <T> void logResultList(List<T> objects) {
        String result = objects.stream()
                .map(T::toString)
                .collect(Collectors.joining(", "));
        log.info("Список запросов --> {}", result);
    }
}