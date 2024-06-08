package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.Request;
import ru.practicum.ewm.enums.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEvent_Id(Long eventId);

    List<Request> findAllByRequester_Id(Long userId);

    List<Request> findAllByRequester_IdAndEvent_Id(Long userId, Long eventId);

    List<Request> findALlByStatusAndEventId(RequestStatus status, Long eventId);
}
