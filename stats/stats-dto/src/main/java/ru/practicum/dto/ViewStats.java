package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
