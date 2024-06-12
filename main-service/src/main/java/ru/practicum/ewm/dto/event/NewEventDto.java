package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.entity.Location;
import ru.practicum.ewm.utils.Constant;
import ru.practicum.ewm.utils.validation.TwoHoursLater;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class NewEventDto {
    @Size(min = 20, max = 2000)
    @NotBlank
    private final String annotation;
    private final Long category;
    @Size(min = 20, max = 7000)
    @NotBlank
    private final String description;
    @NotNull
    @TwoHoursLater
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_FORMAT)
    private final LocalDateTime eventDate;
    @NotNull
    private final Location location;
    private final Boolean paid;
    @PositiveOrZero(message = "Неотрицательное значение")
    private final Integer participantLimit;
    private final Boolean requestModeration;
    @Size(min = 3, max = 120)
    private final String title;
}