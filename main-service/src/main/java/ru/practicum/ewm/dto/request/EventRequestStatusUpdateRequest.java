package ru.practicum.ewm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.enums.RequestStatus;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    RequestStatus status;
}
