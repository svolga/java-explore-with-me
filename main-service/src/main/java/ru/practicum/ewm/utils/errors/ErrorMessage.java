package ru.practicum.ewm.utils.errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    String field;
    String error;
    Object value;

    @Override
    public String toString() {
        return String.format("Field: %s. Error: %s. Value: %s", field, error, value);
    }
}

