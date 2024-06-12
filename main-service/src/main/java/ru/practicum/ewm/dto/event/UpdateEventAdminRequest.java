package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.enums.StateAction;
import ru.practicum.ewm.utils.validation.TwoHoursLater;
import ru.practicum.ewm.entity.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.utils.Constant.DATE_TIME_FORMAT;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UpdateEventAdminRequest extends UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private final String annotation;
    private final Long category;
    @Size(min = 20, max = 7000)
    private final String description;
    @JsonProperty("eventDate")
    @JsonFormat(pattern = DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    @Nullable
    @TwoHoursLater
    private final LocalDateTime eventDate;
    private final Location location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final Boolean requestModeration;
    private final StateAction stateAction;
    @Size(min = 3, max = 120)
    private final String title;
}
