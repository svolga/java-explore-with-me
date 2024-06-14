package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsCommentByIdAndUserId(Long id, Long userId);

    List<Comment> findCommentsByEvent_Id(Long userId, Pageable pageable);

    List<Comment> findCommentsByUser_Id(Long userId, Pageable pageable);

    List<Comment> findCommentsByUser_IdAndEvent_Id(Long userId, Long eventId, Pageable pageable);
}