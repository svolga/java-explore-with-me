package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Data
@Value
public class EndpointHit {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String app;

    @NotNull
    @Size(max = 512)
    private String uri;

    @NotNull
    @Size(max = 16)
    private String ip;

    @NotNull
    private String timestamp;
}
