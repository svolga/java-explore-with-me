package ru.practicum.ewm.controller.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.service.comment.CommentService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.USERS_URL + Constant.USER_ID_PATH_VARIABLE + Constant.COMMENTS_URL)
@Validated
public class CommentPrivateController {

    private final CommentService commentService;

    @DeleteMapping(Constant.COMMENT_ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommentDto deleteCommentByUser(@PathVariable final Long userId, @PathVariable final Long commentId) {
        log.info("Удалить Comment пользователем для userId --> {}, commentId --> {}", userId, commentId);
        return commentService.deleteCommentByUser(userId, commentId);
    }

    @GetMapping(Constant.COMMENTS_URL)
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsByUserAndEvent(@Positive @PathVariable Long userId,
                                                      @RequestParam(
                                                              name = Constant.PARAMETER_SORT,
                                                              required = false) String sort,
                                                      @PositiveOrZero @RequestParam(
                                                              name = Constant.PARAMETER_FROM,
                                                              defaultValue = Constant.DEFAULT_ZERO) Integer from,
                                                      @Positive @RequestParam(
                                                              name = Constant.PARAMETER_SIZE,
                                                              defaultValue = Constant.DEFAULT_TEN) Integer size) {
        log.info("Получить комментарии с доступом private для userId --> {}, from --> {}, size --> {}"
                , userId, from, size);
        return commentService.getCommentsByUser(userId, sort, from, size);
    }

}
