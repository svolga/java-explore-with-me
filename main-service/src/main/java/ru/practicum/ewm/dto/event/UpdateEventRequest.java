package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.enums.StateAction;
import ru.practicum.ewm.entity.Location;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    protected String annotation;
    protected Long category;
    protected String description;
    protected LocalDateTime eventDate;
    protected Location location;
    protected Boolean paid;
    protected Integer participantLimit;
    protected Boolean requestModeration;
    protected StateAction stateAction;
    protected String title;
}
