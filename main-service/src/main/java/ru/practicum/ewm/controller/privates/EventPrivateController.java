package ru.practicum.ewm.controller.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.USERS_URL + Constant.USER_ID_PATH_VARIABLE + Constant.EVENTS_URL)
public class EventPrivateController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsByUser(@Positive @PathVariable Long userId,
                                               @PositiveOrZero @RequestParam(
                                                       name = Constant.FROM_PARAMETER_NAME,
                                                       defaultValue = Constant.ZERO_DEFAULT_VALUE) Integer from,
                                               @Positive @RequestParam(
                                                       name = Constant.SIZE_PARAMETER_NAME,
                                                       defaultValue = Constant.TEN_DEFAULT_VALUE) Integer size) {
        log.info("Получить события с доступом private для userId --> {}, from --> {}, size --> {}", userId, from, size);
        return eventService.getEventsByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@Positive @PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto event) {
        log.info("Создать событие с доступом private для userId --> {}"
                + ", NewEventDto --> {}", userId, event);
        return eventService.addEvent(userId, event);
    }

    @GetMapping(Constant.EVENT_ID_PATH_VARIABLE)
    public EventFullDto getEventByUser(@Positive @PathVariable Long userId,
                                       @Positive @PathVariable Long eventId) {
        log.info("Получение полной информации о событии eventId --> {}  добавленном текущим пользователем userId --> {}");
        return eventService.getEventByUser(userId, eventId);

    }

    @PatchMapping(Constant.EVENT_ID_PATH_VARIABLE)
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventUserRequest event) {
        log.info("Изменение события добавленного текущим пользователем, userId --> {}, eventId --> {}, " +
                "event --> {} ", userId, eventId, event);
        return eventService.updateEventByUser(userId, eventId, event);

    }

    @GetMapping(Constant.EVENT_ID_PATH_VARIABLE + Constant.REQUESTS_URL)
    public List<ParticipationRequestDto> getParticipationRequests(@Positive @PathVariable Long userId,
                                                                  @Positive @PathVariable Long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя userId --> {}, eventId --> {} ",
                userId, eventId);
        return eventService.getParticipationRequests(userId, eventId);

    }

    @PatchMapping(Constant.EVENT_ID_PATH_VARIABLE + Constant.REQUESTS_URL)
    public EventRequestStatusUpdateResult updateRequestsStatus(@Positive @PathVariable(name = "userId") Long userId,
                                                               @Positive @PathVariable(name = "eventId") Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя," +
                        "userId --> {}, eventId --> {}, EventRequestStatusUpdateRequest --> {}",
                userId, eventId, request);
        return eventService.updateRequestsStatus(userId, eventId, request);
    }

}
