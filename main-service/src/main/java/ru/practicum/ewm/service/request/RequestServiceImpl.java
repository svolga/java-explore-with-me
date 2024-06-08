package ru.practicum.ewm.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.Request;
import ru.practicum.ewm.enums.RequestStatus;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.utils.errors.ErrorConstants;
import ru.practicum.ewm.utils.mapper.RequestMapper;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.utils.logger.ListLogger;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.utils.errors.exceptions.NotAllowedException;
import ru.practicum.ewm.utils.errors.exceptions.NotFoundException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {

        checkRequestNotExists(userId, eventId);
        User requester = getUserOrThrowException(userId);
        Event event = getEventOrThrowException(eventId);
        checkEventIsPublished(event.getState());
        checkUserIsNotInitiator(userId, event.getInitiator().getId());
        Integer limit = event.getParticipantLimit();
        Integer confirmed = event.getConfirmedRequests();
        log.info("Получен запрос для eventId --> {} с limit --> {}, confirmed --> {}", eventId, limit, confirmed);
        checkParticipationLimitHasNotReached(limit, confirmed);

        Request request = buildRequest(requester, event);
        request = confirmRequestIfEventHasNoLimits(request, limit, event.getRequestModeration());
        Request newRequest = requestRepository.save(request);
        if (newRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            eventRepository.save(event.toBuilder().confirmedRequests(++confirmed).build());
        }
        log.info("Добавлен запрос с requestId --> {} userId --> {}, статус --> {},  событие --> {},  " +
                        "количество подтвержденных запросов --> {}",
                request.getId(), userId, newRequest.getStatus(), eventId, confirmed);
        return RequestMapper.toParticipationRequestDto(newRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {

        Request request = requestRepository.getReferenceById(requestId);
        checkUserIsRequester(userId, request.getRequester().getId());
        Request canceledRequest = requestRepository.save(request.toBuilder().status(RequestStatus.CANCELED).build());
        log.info("Отменен запрос {}", canceledRequest);
        return RequestMapper.toParticipationRequestDto(canceledRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequests(Long userId) {
        checkUserExists(userId);
        log.info("Сделан запрос на получение списка запросов пользователя с userId --> {}", userId);
        List<Request> requests = requestRepository.findAllByRequester_Id(userId);
        ListLogger.logResultList(requests);
        return RequestMapper.toParticipationRequestDtoList(requests);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь не найден с userId --> %s", userId));
        }
    }

    private void checkEventIsPublished(String state) {
        if (!String.valueOf(EventState.PUBLISHED).equalsIgnoreCase(state)) {
            throw new NotAllowedException(ErrorConstants.EVENT_IS_NOT_PUBLISHED_YET);
        }
    }

    private void checkRequestNotExists(Long userId, Long eventId) {
        if (requestRepository
                .findAllByRequester_IdAndEvent_Id(userId, eventId)
                .stream()
                .findAny()
                .isPresent()) {
            throw new NotAllowedException(ErrorConstants.REPEATED_REQUEST);
        }
    }

    private void checkParticipationLimitHasNotReached(Integer participantLimit, Integer confirmedRequests) {
        if (participantLimit != 0 && participantLimit.equals(confirmedRequests)) {
            throw new NotAllowedException(ErrorConstants.LIMIT);
        }
    }

    private void checkUserIsRequester(Long userId, Long requesterId) {
        if (!userId.equals(requesterId)) {
            throw new NotAllowedException(ErrorConstants.ONLY_FOR_REQUESTER);
        }
    }

    private void checkUserIsNotInitiator(Long userId, Long initiatorId) {
        if (userId.equals(initiatorId)) {
            throw new NotAllowedException(ErrorConstants.NOT_FOR_INITIATOR);
        }
    }

    private Request buildRequest(User requester, Event event) {
        return Request.builder()
                .requester(requester)
                .event(event)
                .status(RequestStatus.PENDING)
                .created(LocalDateTime.now())
                .build();
    }

    private Request confirmRequestIfEventHasNoLimits(Request request, Integer participantLimit,
                                                     Boolean requestModeration) {

        if (participantLimit == 0 || Boolean.FALSE.equals(requestModeration)) {
            log.info("Limit не задан, модерация не задана, новый статус запрос от {}"
                            + " для участия в событии --> {} равна --> {}",
                    request.getRequester(), request.getEvent(), RequestStatus.CONFIRMED.name());
            return request.toBuilder().status(RequestStatus.CONFIRMED).build();
        }
        return request;
    }

    private Event getEventOrThrowException(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("Event", eventId)));
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("User", userId)));
    }
}