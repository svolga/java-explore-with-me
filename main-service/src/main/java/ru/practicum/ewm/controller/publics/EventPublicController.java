package ru.practicum.ewm.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.service.comment.CommentService;
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
    private final CommentService commentService;

    @GetMapping
    public List<EventShortDto> getPublicEvents(
            @RequestParam(
                    name = Constant.PARAMETER_TEXT,
                    required = false) String text,
            @RequestParam(
                    name = Constant.PARAMETER_CATEGORIES,
                    required = false) List<Long> categories,
            @RequestParam(
                    name = Constant.PARAMETER_PAID,
                    required = false) Boolean paid,
            @RequestParam(
                    name = Constant.PARAMETER_RANGE_START,
                    required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(
                    name = Constant.PARAMETER_RANGE_END,
                    required = false)
            @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(
                    name = Constant.PARAMETER_ONLY_AVAILABLE,
                    defaultValue = Constant.DEFAULT_FALSE) Boolean onlyAvailable,
            @RequestParam(
                    name = Constant.PARAMETER_SORT,
                    required = false) String sort,
            @RequestParam(
                    name = Constant.PARAMETER_FROM,
                    defaultValue = Constant.DEFAULT_ZERO) @PositiveOrZero Integer from,
            @RequestParam(
                    name = Constant.PARAMETER_SIZE,
                    defaultValue = Constant.DEFAULT_TEN) @Positive Integer size,
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

    @GetMapping(Constant.EVENT_ID_PATH_VARIABLE + Constant.COMMENTS_URL)
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsByEvent(@Positive @PathVariable Long eventId,
                                               @RequestParam(
                                                       name = Constant.PARAMETER_SORT,
                                                       required = false) String sort,
                                               @PositiveOrZero @RequestParam(
                                                       name = Constant.PARAMETER_FROM,
                                                       defaultValue = Constant.DEFAULT_ZERO) Integer from,
                                               @Positive @RequestParam(
                                                       name = Constant.PARAMETER_SIZE,
                                                       defaultValue = Constant.DEFAULT_TEN) Integer size) {
        log.info("Получить комментарии с доступом public для eventId --> {}, from --> {}, size --> {}", eventId, from, size);
        return commentService.getCommentsByEvent(eventId, sort, from, size);
    }

}