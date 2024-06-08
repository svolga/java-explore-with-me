package ru.practicum.ewm.controller.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.service.request.RequestService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.USERS_URL + Constant.USER_ID_PATH_VARIABLE + Constant.REQUESTS_URL)
@Validated
public class RequestPrivateController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getUserParticipationRequests(@Positive @PathVariable Long userId) {
        log.info("Получить запросы на участие для userId --> {}", userId);
        return requestService.getParticipationRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@Positive @PathVariable Long userId,
                                                           @Positive @RequestParam Long eventId) {
        log.info("Добавить запрос на участие для пользователя userId --> {}, на событие eventId --> {}",
                userId, eventId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping(Constant.REQUEST_ID_PATH_VARIABLE + Constant.CANCEL_URL)
    public ParticipationRequestDto cancelParticipationRequest(@Positive @PathVariable Long userId,
                                                              @Positive @PathVariable Long requestId) {
        log.info("Изменить (отменить) запрос на участие для userId --> {},  requestId --> {}", userId, requestId);
        return requestService.cancelParticipationRequest(userId, requestId);
    }
}
