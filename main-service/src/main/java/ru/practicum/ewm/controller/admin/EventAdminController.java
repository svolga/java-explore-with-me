package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.ADMIN_URL + Constant.EVENTS_URL)
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsByAdmin(
            @RequestParam(
                    name = Constant.PARAMETER_USERS,
                    required = false) List<Long> users,
            @RequestParam(
                    name = Constant.PARAMETER_STATES,
                    required = false) List<String> states,
            @RequestParam(
                    name = Constant.PARAMETER_CATEGORIES,
                    required = false) List<Long> categories,
            @RequestParam(
                    name = Constant.PARAMETER_RANGE_START,
                    required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(
                    name = Constant.PARAMETER_RANGE_END,
                    required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(
                    name = Constant.PARAMETER_FROM,
                    defaultValue = Constant.DEFAULT_ZERO) Integer from,
            @Positive @RequestParam(
                    name = Constant.PARAMETER_SIZE,
                    defaultValue = Constant.DEFAULT_TEN) Integer size) {
        log.info("Получить Events для админа для users --> {}, states --> {}, categories --> {}, \n"
                        + "rangeStart --> {}, rangeEnd --> {}, значение from --> {}, size --> {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(Constant.EVENT_ID_PATH_VARIABLE)
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @Valid @RequestBody(required = false) UpdateEventAdminRequest request) {
        log.info("Изменить Event админом для eventId --> {}, request --> {}", eventId, request);
        return eventService.updateEventByAdmin(eventId, request);
    }
}