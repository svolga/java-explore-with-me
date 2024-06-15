package ru.practicum.ewm.dto.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.utils.Constant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
public class UpdateCommentDto {
    @NotNull
    @Positive
    private Long id;
    @NotNull
    @Positive
    private Long eventId;
    @Size(min = Constant.MIN_LENGTH_COMMENT, max = Constant.MAX_LENGTH_COMMENT)
    @NotBlank
    private String text;
}
