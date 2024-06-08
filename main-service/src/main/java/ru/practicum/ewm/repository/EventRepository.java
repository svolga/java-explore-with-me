package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.entity.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event AS e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN :users) " +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
            "AND ((cast(:start as timestamp) IS NULL OR e.eventDate >= :start) " +
            "AND ((cast(:end as timestamp) IS NULL OR e.eventDate <= :end)))")
    List<Event> findForAdmin(List<Long> users, List<String> states, List<Long> categories,
                             LocalDateTime start, LocalDateTime end, Pageable pageable);


    @Query("SELECT e FROM Event AS e " +
            "WHERE ((:text) IS NULL " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
            "AND ((:paid) IS NULL OR e.paid = :paid) " +
            "AND ( e.eventDate >= :start) " +
            "AND ( e.eventDate <= :end) " +
            "AND ( e.state = :published) " +
            "AND ( e.confirmedRequests < e.participantLimit OR e.participantLimit = 0) ")
    List<Event> findAvailableForPublic(String text, List<Long> categories, Boolean paid,
                                       LocalDateTime start, LocalDateTime end, String published, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE ((:text) IS NULL " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
            "AND ((:paid) IS NULL OR e.paid = :paid) " +
            "AND ( e.eventDate >= :start) " +
            "AND ( e.eventDate <= :end) " +
            "AND ( e.state = :published) ")
    List<Event> findAllForPublic(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                                 LocalDateTime end,
                                 String published, Pageable pageable);

    Optional<Event> findByIdAndState(Long eventId, String state);

    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    boolean existsByCategory_Id(Long catid);

    List<Event> findAllByIdIn(List<Long> ids);
}