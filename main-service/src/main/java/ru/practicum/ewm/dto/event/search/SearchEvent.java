package ru.practicum.ewm.dto.event.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class SearchEvent {
    private final String text;
    private final List<Long> categories;
    private final List<Long> users;
    private List<String> states;
    private final Boolean paid;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final Boolean onlyAvailable;
    private final String sort;
    private final Integer from;
    private final Integer size;
}