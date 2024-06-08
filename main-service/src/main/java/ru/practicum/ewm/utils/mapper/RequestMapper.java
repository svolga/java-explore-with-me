package ru.practicum.ewm.utils.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.entity.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

    public static List<ParticipationRequestDto> toParticipationRequestDtoList(List<Request> users) {
        return users.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

}
