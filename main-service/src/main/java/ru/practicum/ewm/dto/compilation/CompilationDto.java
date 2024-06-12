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
    private Integer id;
    private List<EventShortDto> events;
    private String title;
    private Boolean pinned;
}
