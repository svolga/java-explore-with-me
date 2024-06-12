package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.entity.Location;
import ru.practicum.ewm.enums.EventState;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constant.DATE_TIME_FORMAT;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class EventFullDto {
    private final Long id;
    private final String annotation;
    private final CategoryDto category;
    private final Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private final LocalDateTime createdOn;
    private final String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private final LocalDateTime eventDate;
    private final UserShortDto initiator;
    private final Location location;
    private final Boolean paid;
    @PositiveOrZero(message = "Неотрицательное значение")
    private final Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private final LocalDateTime publishedOn;
    private final Boolean requestModeration;
    private final EventState state;
    private final String title;
    private final Long views;
}
