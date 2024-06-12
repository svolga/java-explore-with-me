package ru.practicum.ewm.service.event;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.search.SearchEvent;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Component
public interface EventService {

    List<EventShortDto> getPublicEvents(SearchEvent searchEvent, HttpServletRequest request);

    EventFullDto getPublicEventById(Long id, HttpServletRequest request);

    List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto addEvent(Long userId, @Valid NewEventDto event);

    EventFullDto getEventByUser(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest event);

    List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId);

    List<EventFullDto> getEventsByAdmin(SearchEvent searchEvent);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest request);

    EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest request);
}