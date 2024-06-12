package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class CompilationDto {
    private final Integer id;
    private final List<EventShortDto> events;
    private final String title;
    private final Boolean pinned;
}
