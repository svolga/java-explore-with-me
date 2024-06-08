package ru.practicum.ewm.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(Constant.EVENTS_URL)
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getPublicEvents(
            @RequestParam(
                    name = Constant.TEXT_PARAMETER_NAME,
                    required = false) String text,
            @RequestParam(
                    name = Constant.CATEGORIES_PARAMETER_NAME,
                    required = false) List<Long> categories,
            @RequestParam(
                    name = Constant.PAID_PARAMETER_NAME,
                    required = false) Boolean paid,
            @RequestParam(
                    name = Constant.RANGE_START_PARAMETER_NAME,
                    required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(
                    name = Constant.RANGE_END_PARAMETER_NAME,
                    required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(
                    name = Constant.ONLY_AVAILABLE_PARAM_NAME,
                    defaultValue = Constant.FALSE_DEFAULT_VALUE) Boolean onlyAvailable,
            @RequestParam(
                    name = Constant.SORT_PARAMETER_NAME,
                    required = false) String sort,
            @RequestParam(
                    name = Constant.FROM_PARAMETER_NAME,
                    defaultValue = Constant.ZERO_DEFAULT_VALUE) @PositiveOrZero Integer from,
            @RequestParam(
                    name = Constant.SIZE_PARAMETER_NAME,
                    defaultValue = Constant.TEN_DEFAULT_VALUE) @Positive Integer size,
            HttpServletRequest request) {
        log.info("Public запрос на получение событий с возможностью фильтрации text --> {}, categories --> {}, " +
                        "paid --> {}, rangeStart --> {}, rangeEnd --> {}, onlyAvailable --> {}, sort --> {}, " +
                        "from --> {}, size --> {}, RemoteAddr --> {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr());

        return eventService
                .getPublicEvents(text, categories, paid, rangeStart, rangeEnd,
                        onlyAvailable, sort, from, size, request);
    }

    @GetMapping(Constant.EVENT_ID_PATH_VARIABLE)
    public EventFullDto getPublicEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору eventId --> {}",
                eventId);
        return eventService.getPublicEventById(eventId, request);
    }

}
