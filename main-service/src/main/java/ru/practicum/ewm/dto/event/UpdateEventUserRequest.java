package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.entity.Location;
import ru.practicum.ewm.enums.StateAction;
import ru.practicum.ewm.utils.Constant;
import ru.practicum.ewm.utils.validation.TwoHoursLater;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest extends UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonProperty("eventDate")
    @JsonFormat(pattern = Constant.DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    @Nullable
    @TwoHoursLater
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero(message = "Значение не должно быть отрицательным")
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(min = 3, max = 120)
    String title;
}
