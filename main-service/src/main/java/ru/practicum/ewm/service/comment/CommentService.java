package ru.practicum.ewm.service.comment;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.dto.comment.UpdateCommentDto;

import javax.validation.Valid;
import java.util.List;

@Component
public interface CommentService {

    List<CommentDto> getComments(String sort, Integer from, Integer size);

    List<CommentDto> getCommentsByEvent(Long eventId, String sort, Integer from, Integer size);

    List<CommentDto> getCommentsByUser(Long userId, String sort, Integer from, Integer size);

    List<CommentDto> getCommentsByUserAndEvent(Long userId, long eventId, String sort, Integer from, Integer size);

    CommentDto getCommentById(Long commentId);

    CommentDto addComment(Long userId, @Valid NewCommentDto comment);

    CommentDto deleteCommentByAdmin(Long commentId);

    CommentDto deleteCommentByUser(Long userId, Long commentId);

    CommentDto updateCommentByAdmin(UpdateCommentDto updateCommentDto);

    CommentDto updateCommentByUser(Long userId, UpdateCommentDto updateCommentDto);
}
