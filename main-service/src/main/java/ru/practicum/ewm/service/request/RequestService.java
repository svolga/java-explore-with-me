package ru.practicum.ewm.service.request;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

@Component
public interface RequestService {

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getParticipationRequests(Long userId);

}
