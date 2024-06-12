package ru.practicum.ewm.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class CategoryDto {
    private final  Long id;
    @Size(min = 1, max = 50)
    @NotBlank
    private final String name;
}
