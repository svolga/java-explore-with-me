package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class NewCompilationDto {
    private final List<Long> events;
    @Size(min = 1, max = 50)
    @NotBlank
    private final String title;
    private final Boolean pinned;
}
