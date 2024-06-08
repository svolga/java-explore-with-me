package ru.practicum.ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class NewUserRequest {
    @NotBlank(message = "должно быть заполнено")
    @Size(min = 2, max = 250)
    private String name;
    @NotBlank(message = "должно быть заполнено")
    @Size(min = 6, max = 254)
    @Email
    private String email;
}
