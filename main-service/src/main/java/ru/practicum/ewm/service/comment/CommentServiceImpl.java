package ru.practicum.ewm.service.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.entity.Comment;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.utils.errors.ErrorConstants;
import ru.practicum.ewm.utils.errors.exceptions.NotAllowedException;
import ru.practicum.ewm.utils.errors.exceptions.NotFoundException;
import ru.practicum.ewm.utils.logger.ListLogger;
import ru.practicum.ewm.utils.mapper.CommentMapper;
import ru.practicum.ewm.utils.paging.Paging;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto commentDto) {
        User user = getUserOrThrowException(userId);
        Event event = getEventOrThrowException(eventId);
        Comment newComment = commentRepository.save(CommentMapper.toCommentEntity(commentDto, user, event));
        CommentDto newCommentDto = CommentMapper.toCommentDto(newComment);
        log.info("Добавлен Comment --> {}", newCommentDto);
        return newCommentDto;
    }

    @Override
    public CommentDto updateCommentByAdmin(Long commentId, NewCommentDto commentDto) {
        Comment comment = getCommentOrThrowException(commentId);
        Comment updated = comment.toBuilder()
                .text(commentDto.getText())
                .build();

        Comment updatedComment = commentRepository.save(updated);
        CommentDto updatedCommentDto = CommentMapper.toCommentDto(updatedComment);
        log.info("Обновлен Comment --> {}", updatedCommentDto);
        return updatedCommentDto;
    }

    @Override
    public CommentDto updateCommentByUser(Long userId, Long eventId, Long commentId, NewCommentDto commentDto) {
        getUserOrThrowException(userId);
        checkIsInitiator(userId, commentId);
        getEventOrThrowException(eventId);
        return updateCommentByAdmin(commentId, commentDto);
    }

    @Override
    public List<CommentDto> getComments(String sort, Integer from, Integer size) {
        List<Comment> comments = commentRepository.findAll(Paging.getPageable(from, size)).getContent();
        ListLogger.logResultList(comments);
        return CommentMapper.toCommentDtoList(comments);
    }

    @Override
    public List<CommentDto> getCommentsByEvent(Long eventId, String sort, Integer from, Integer size) {
        getEventOrThrowException(eventId);
        List<Comment> comments = commentRepository.findCommentsByEvent_Id(eventId, Paging.getPageable(from, size));
        return CommentMapper.toCommentDtoList(comments);
    }

    @Override
    public List<CommentDto> getCommentsByUser(Long userId, String sort, Integer from, Integer size) {
        getUserOrThrowException(userId);
        List<Comment> comments = commentRepository.findCommentsByUser_Id(userId, Paging.getPageable(from, size));
        return CommentMapper.toCommentDtoList(comments);
    }

    @Override
    public List<CommentDto> getCommentsByUserAndEvent(Long userId, long eventId, String sort, Integer from, Integer size) {
        getUserOrThrowException(userId);
        getEventOrThrowException(eventId);
        List<Comment> comments = commentRepository.findCommentsByUser_IdAndEvent_Id(userId, eventId, Paging.getPageable(from, size));
        return CommentMapper.toCommentDtoList(comments);
    }

    @Override
    public CommentDto deleteCommentByAdmin(Long commentId) {
        CommentDto deletedComment = CommentMapper.toCommentDto(getCommentOrThrowException(commentId));
        commentRepository.deleteById(commentId);
        log.info("Удален комментарий --> {}", deletedComment);
        return deletedComment;
    }

    @Override
    public CommentDto deleteCommentByUser(Long userId, Long commentId) {
        getUserOrThrowException(userId);
        checkIsInitiator(userId, commentId);
        return deleteCommentByAdmin(commentId);
    }

    private Comment getCommentOrThrowException(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("Comment", commentId)));
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("User", userId)));
    }

    private Event getEventOrThrowException(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("Event", eventId)));
    }

    private void checkIsInitiator(Long userId, Long commentId) {
        log.info("Проверка, что пользователь с userId --> {} является владельцем комментария commentId --> {}",
                userId, commentId);
        if (!commentRepository.existsCommentByIdAndUserId(commentId, userId)) {
            throw new NotAllowedException(ErrorConstants.ONLY_FOR_INITIATOR);
        }
    }
}