package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.UpdateCommentDto;
import ru.practicum.ewm.service.comment.CommentService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.ADMIN_URL + Constant.COMMENTS_URL)
public class CommentAdminController {
    private final CommentService commentService;

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateCommentByAdmin(@Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Изменить Comment админом для updateCommentDto --> {}", updateCommentDto);
        return commentService.updateCommentByAdmin(updateCommentDto);
    }

    @DeleteMapping(Constant.COMMENT_ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommentDto deleteCommentByAdmin(@Positive @PathVariable Long commentId) {
        log.info("Удалить Comment админом для commentId --> {}", commentId);
        return commentService.deleteCommentByAdmin(commentId);
    }

}