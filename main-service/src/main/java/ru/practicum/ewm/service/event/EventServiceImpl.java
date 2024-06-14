package ru.practicum.ewm.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Location;
import ru.practicum.ewm.entity.Request;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.enums.RequestStatus;
import ru.practicum.ewm.enums.StateAction;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.LocationRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.statistics.StatisticsService;
import ru.practicum.ewm.utils.Constant;
import ru.practicum.ewm.utils.errors.ErrorConstants;
import ru.practicum.ewm.utils.errors.exceptions.NotAllowedException;
import ru.practicum.ewm.utils.errors.exceptions.NotFoundException;
import ru.practicum.ewm.utils.logger.ListLogger;
import ru.practicum.ewm.utils.mapper.EnumMapper;
import ru.practicum.ewm.utils.mapper.EventMapper;
import ru.practicum.ewm.utils.mapper.RequestMapper;
import ru.practicum.ewm.utils.validation.EnumTypeValidation;
import ru.practicum.ewm.utils.validation.EventTimeValidator;
import ru.practicum.ewm.utils.paging.Paging;
import ru.practicum.ewm.enums.SortType;
import ru.practicum.ewm.utils.validation.TwoHoursLater;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    private final LocationRepository locationRepository;
    private final StatisticsService statisticsService;

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto dto) {

        User initiator = getUserOrThrowException(userId);
        Category category = getCategoryOrThrowException(dto.getCategory());
        Location location = locationRepository.save(dto.getLocation());

        Event event = EventMapper.toEventEntity(dto, location, initiator, category);
        Event newEvent = eventRepository.save(event);
        log.info("Добавлен для id --> {}, Event --> {}", newEvent.getId(), newEvent);
        return EventMapper.toEventFullDto(newEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getPublicEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository
                .findByIdAndState(eventId, EventState.PUBLISHED.name())
                .orElseThrow(() -> new NotFoundException(
                        ErrorConstants.getNotFoundMessage("Event", eventId)));
        log.info("start saveStats, request --> {}", request);
        statisticsService.saveStats(request);
        log.info("Start getStats");
        Long views = statisticsService.getStats(
                event.getPublishedOn(), LocalDateTime.now(), List.of(request.getRequestURI())).get(eventId);
        log.info("Do mapping with views --> {}", views);

        Event eventWithStat = EventMapper.toEventWithStat(event, views);
        Event savedEvent = eventRepository.save(eventWithStat);
        EventFullDto result = EventMapper.toEventFullDto(savedEvent);
        log.info("Найден для eventId --> {},  Event --> {}", eventId, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Boolean onlyAvailable, String sort, Integer from, Integer size,
                                               HttpServletRequest request) {

        SortType sortType = (sort == null) ? null : EnumTypeValidation.getValidSortType(sort);
        LocalDateTime start = (rangeStart == null) ? LocalDateTime.now() : rangeStart;
        LocalDateTime end = (rangeEnd == null) ? LocalDateTime.now().plusYears(100) : rangeEnd;

        EventTimeValidator.checkStartTimeIsAfterEnd(start, end);

        List<Event> events;
        if (onlyAvailable) {
            events = eventRepository.findAvailableForPublic(
                    text, categories, paid, start, end,
                    String.valueOf(EventState.PUBLISHED), Paging.getPageable(from, size, sortType));

        } else {
            events = eventRepository.findAllForPublic(
                    text, categories, paid, start, end,
                    String.valueOf(EventState.PUBLISHED), Paging.getPageable(from, size, sortType));
        }

        statisticsService.saveStats(request);

        List<Event> eventsWithViews = Collections.emptyList();
        if (!events.isEmpty()) {
            LocalDateTime oldestEventPublishedOn = events.stream()
                    .min(Comparator.comparing(Event::getPublishedOn)).map(Event::getPublishedOn).stream()
                    .findFirst().orElseThrow();
            List<String> uris = getListOfUri(events, request.getRequestURI());

            Map<Long, Long> views = statisticsService.getStats(oldestEventPublishedOn, LocalDateTime.now(), uris);
            eventsWithViews = events
                    .stream()
                    .map(event -> EventMapper.toEventWithStat(event, views.get(event.getId())))
                    .collect(Collectors.toList());
        }
        List<Event> savedEvents = eventRepository.saveAll(eventsWithViews);
        List<EventShortDto> resultList = EventMapper.toEventShortDtoList(savedEvents);
        ListLogger.logResultList(resultList);
        return resultList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findAllByInitiator_Id(userId, Paging.getPageable(from, size));
        List<EventShortDto> resultList = EventMapper.toEventShortDtoList(events);
        ListLogger.logResultList(resultList);
        return resultList;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        Event event = getEventOrThrowException(eventId);
        EventFullDto result = EventMapper.toEventFullDto(event);
        log.info("Найден Event для id --> {},  EventFullDto --> {}", eventId, result);
        return result;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest request) {
        log.info("updateEventByUser для userId --> {}, eventId --> {}, UpdateEventUserRequest --> {}",
                userId, eventId, request);
        StateAction action = (request.getStateAction() == null) ? null :
                EnumTypeValidation.getValidUserAction(request.getStateAction());
        Event event = getEventOrThrowException(eventId);
        EventTimeValidator.checkStartTimeIsValid(event.getEventDate());
        checkEventStateIsCanceledOrPending(event.getState());
        checkIsInitiator(userId, event.getInitiator().getId());

        Event updatedEvent = eventRepository.save(updateNonNullFields(event, request, action));
        EventFullDto result = EventMapper.toEventFullDto(updatedEvent);
        log.info("Обновлен Event для id --> {}, EventFullDto --> {}", eventId, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {

        Event event = getEventOrThrowException(eventId);
        checkIsInitiator(userId, event.getInitiator().getId());

        List<Request> requests = requestRepository.findAllByEvent_Id(eventId);
        List<ParticipationRequestDto> resultList = RequestMapper.toParticipationRequestDtoList(requests);
        ListLogger.logResultList(resultList);
        return resultList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Integer from, Integer size) {
        if (states != null) {
            EnumTypeValidation.checkValidEventStates(states);
        }
        List<Event> events = eventRepository.findForAdmin(
                users, states, categories, rangeStart, rangeEnd, Paging.getPageable(from, size));
        List<EventFullDto> resultList = EventMapper.toEventFullDtoList(events);
        ListLogger.logResultList(resultList);
        return resultList;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest request) {

        StateAction action = (request.getStateAction() == null) ? null :
                EnumTypeValidation.getValidAdminAction(request.getStateAction());

        Event event = getEventOrThrowException(eventId);
        checkIsEventTimeIsNotTooLateToUpdateByAdmin(event.getEventDate());
        checkEventStateIsPending(event.getState());
        if (StateAction.PUBLISH_EVENT.equals(action)) {
            event = event.toBuilder().publishedOn(LocalDateTime.now()).build();
        }
        Event updatedEvent = eventRepository.save(updateNonNullFields(event, request, action));
        EventFullDto result = EventMapper.toEventFullDto(updatedEvent);
        log.info("Обновление для Event по id --> {},  EventFullDto --> {}", eventId, result);
        return result;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest request) {

        Event event = eventRepository.getReferenceById(eventId);
        checkIsInitiator(userId, event.getInitiator().getId());

        List<Request> requests = requestRepository.findAllById(request.getRequestIds());
        checkAllRequestsStatusIsPending(requests);
        RequestStatus status = request.getStatus();

        long limit = event.getParticipantLimit();

        if (status.equals(RequestStatus.REJECTED)) {
            return rejectAllRequests(requests);
        }
        if (checkHasNoLimitAndModeration(limit, event.getRequestModeration())) {
            saveEventWithUpdatedNumberOfConfirmedRequests(event, requests.size());
            return confirmAllRequests(requests);
        }

        return resolveStatus(event, requests);
    }

    private Event getEventOrThrowException(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("Event", eventId)));
    }

    private Category getCategoryOrThrowException(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorConstants.getNotFoundMessage("Category", categoryId)));
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("User", userId)));
    }

    private EventRequestStatusUpdateResult resolveStatus(Event event, List<Request> requests) {

        int limit = event.getParticipantLimit();
        int confirmed = event.getConfirmedRequests();

        checkIsEventAvailable(confirmed, limit);

        int breakPoint = limit - confirmed;
        int countRequests = requests.size();
        log.info("Количество запросов для подтверждения --> {}, доступно {}: ",
                countRequests, breakPoint);
        List<Request> availableToConfirm;
        List<Request> mustBeRejected;
        List<Request> pendingRest;

        if (countRequests < breakPoint) {
            saveEventWithUpdatedNumberOfConfirmedRequests(event, countRequests);
            log.info("Доступно запросов для подтверждения --> {}", countRequests);
            return confirmAllRequests(requests);
        } else if (countRequests == breakPoint) {
            availableToConfirm = requests;
            confirmAllRequests(availableToConfirm);
            mustBeRejected = getRestOfPendingRequests(event.getId());
            rejectAllRequests(mustBeRejected);
            saveEventWithUpdatedNumberOfConfirmedRequests(event, availableToConfirm.size());
            log.info("Доступно для подтверждения запросов --> {}, для отклонения --> {}," +
                            " Недоступное событие --> {}",
                    availableToConfirm.size(), mustBeRejected.size(), event.getId());
            return constructResult(availableToConfirm, mustBeRejected);
        } else {
            availableToConfirm = requests.subList(0, breakPoint);
            confirmAllRequests(availableToConfirm);
            saveEventWithUpdatedNumberOfConfirmedRequests(event, availableToConfirm.size());
            mustBeRejected = requests.subList(breakPoint, countRequests);
            rejectAllRequests(mustBeRejected);
            pendingRest = getRestOfPendingRequests(event.getId());
            rejectAllRequests(pendingRest);
            log.info("Можно подтвердить {} requests, отклоняем {} из текущей сессии и все ожидающие: {}."
                            + " Event {} не доступен",
                    availableToConfirm.size(), requests.size() - availableToConfirm.size(),
                    pendingRest.size(), event.getId());
            return constructResult(availableToConfirm, mustBeRejected);
        }
    }

    private EventRequestStatusUpdateResult rejectAllRequests(List<Request> requests) {
        List<Request> savedRequests = saveRequestsWithNewStatus(requests, RequestStatus.REJECTED);
        return constructResult(Collections.emptyList(), savedRequests);
    }

    private EventRequestStatusUpdateResult confirmAllRequests(List<Request> requests) {
        List<Request> savedRequests = saveRequestsWithNewStatus(requests, RequestStatus.CONFIRMED);
        return constructResult(savedRequests, Collections.emptyList());
    }

    private void saveEventWithUpdatedNumberOfConfirmedRequests(Event event, int number) {
        eventRepository.save(updateNumberOfConfirmedRequests(event, number));
        log.info("Количество подтвержденных запросов для участия в eventId --> {} стало --> {}:",
                event.getId(), number);
    }

    private List<Request> saveRequestsWithNewStatus(List<Request> requests, RequestStatus status) {
        List<Request> savedRequests = requestRepository.saveAll(updateStatusInList(requests, status));
        log.info("Новый статус запросов стал --> {}", status);
        ListLogger.logResultList(savedRequests);
        return savedRequests;
    }

    private void checkIsEventAvailable(Integer confirmed, Integer limit) {
        log.info("Ограничение участников не достигнуто. "
                + "Подтвержденные запросы --> {}. Лимит участников--> {}", confirmed, limit);
        if (Objects.equals(confirmed, limit)) {
            throw new NotAllowedException(ErrorConstants.LIMIT);
        }
    }

    private void checkIsEventTimeIsNotTooLateToUpdateByAdmin(@TwoHoursLater LocalDateTime time) {
        if (time != null) {
            if (time.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new NotAllowedException(ErrorConstants.TIME_IS_LESS_THAN_ONE_HOUR_BEFORE_START);
            }
        }
    }

    private void checkEventStateIsCanceledOrPending(String state) {
        log.info("Проверка статуса события --> {}. Должен быть отменен или со статусом ожидания модерации", state);
        if (EventState.PUBLISHED.name().equals(state)) {
            throw new NotAllowedException(ErrorConstants.EVENT_IS_PUBLISHED);
        }
    }

    private void checkEventStateIsPending(String state) {
        log.info("Проверка статуса события --> {}. Должно быть в ожидании модерации", state);
        if (!EventState.PENDING.name().equals(state)) {
            throw new NotAllowedException(ErrorConstants.EVENT_IS_PUBLISHED);
        }
    }

    private void checkIsInitiator(Long userId, Long initiatorId) {
        log.info("Проверка, что пользователь с userId --> {} является инициатором события для initiatorId --> {}", userId, initiatorId);
        if (!initiatorId.equals(userId)) {
            throw new NotAllowedException(ErrorConstants.ONLY_FOR_INITIATOR);
        }
    }

    private boolean checkHasNoLimitAndModeration(Long limit, Boolean mod) {
        log.info("Проверка, что запрос может быть подтвержден на автомате");
        if (limit == 0 || Boolean.FALSE.equals(mod)) {
            log.info("Ограничение на участие не задано и модерация не требуется, запрос подтвержден");
            return true;
        }
        return false;
    }

    private void checkAllRequestsStatusIsPending(List<Request> requests) {
        log.info("Проверка всех запросов на статус --> pending");
        boolean allPending = requests.stream().allMatch(r -> r.getStatus().equals(RequestStatus.PENDING));
        if (!allPending) {
            log.info("Список запросов для обновления содержит запросы --> not pending");
            throw new NotAllowedException(ErrorConstants.NOT_PENDING);
        }
    }

    private List<Request> updateStatusInList(List<Request> requests, RequestStatus status) {
        return requests.stream().map(request -> updateRequestStatus(request, status))
                .collect(Collectors.toList());
    }

    private Request updateRequestStatus(Request request, RequestStatus status) {
        return request.toBuilder().status(status).build();
    }

    private Event updateNumberOfConfirmedRequests(Event event, Integer delta) {
        return event.toBuilder()
                .confirmedRequests(event.getConfirmedRequests() + delta)
                .build();
    }

    private EventRequestStatusUpdateResult constructResult(List<Request> confirmed, List<Request> rejected) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(RequestMapper.toParticipationRequestDtoList(confirmed))
                .rejectedRequests(RequestMapper.toParticipationRequestDtoList(rejected))
                .build();
    }

    private Event updateNonNullFields(Event event, UpdateEventRequest request, StateAction state) {
        if (request.getAnnotation() != null) {
            event = event.toBuilder()
                    .annotation(request.getAnnotation())
                    .build();
        }
        if (request.getCategory() != null) {
            Category category = categoryRepository.getReferenceById(request.getCategory());
            event = event.toBuilder()
                    .category(category)
                    .build();
        }
        if (request.getDescription() != null) {
            event = event.toBuilder()
                    .description(request.getDescription())
                    .build();
        }
        if (request.getEventDate() != null) {
            LocalDateTime time = request.getEventDate();
            checkIsEventTimeIsNotTooLateToUpdateByAdmin(time);
            event = event.toBuilder()
                    .eventDate(request.getEventDate())
                    .build();
        }
        if (request.getLocation() != null) {
            Location location = locationRepository.save(request.getLocation());
            event = event.toBuilder()
                    .location(location)
                    .build();
        }
        if (request.getPaid() != null) {
            event = event.toBuilder()
                    .paid(request.getPaid())
                    .build();
        }
        if (request.getParticipantLimit() != null) {
            event = event.toBuilder()
                    .participantLimit(request.getParticipantLimit())
                    .build();
        }
        if (request.getParticipantLimit() != null) {
            event = event.toBuilder()
                    .requestModeration(request.getRequestModeration())
                    .build();
        }
        if (state != null) {
            event = event.toBuilder()
                    .state(String.valueOf(EnumMapper.mapToEventState(state)))
                    .build();
        }
        if (request.getTitle() != null) {
            event = event.toBuilder()
                    .title(request.getTitle())
                    .build();
        }
        return event;
    }

    private List<String> getListOfUri(List<Event> events, String uri) {
        return events.stream().map(Event::getId).map(id -> getUriForEvent(uri, id))
                .collect(Collectors.toList());
    }

    private String getUriForEvent(String uri, Long eventId) {
        return uri + Constant.DELIM_URL + eventId;
    }

    private List<Request> getRestOfPendingRequests(Long eventId) {
        return requestRepository.findALlByStatusAndEventId(RequestStatus.PENDING, eventId);
    }
}