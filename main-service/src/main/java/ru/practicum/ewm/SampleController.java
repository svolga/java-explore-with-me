package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHit;
import ru.practicum.ewm.http.StatsClient;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class SampleController {

    private final StatsClient client;

    @PostMapping("hit")
    public ResponseEntity<Object> addHit(@Valid @RequestBody EndpointHit endpointHit) {
        log.info("POST request: endpointHit = {}", endpointHit);
        return client.create(endpointHit);
    }

    @GetMapping("stats")
    public ResponseEntity<Object> getStats(@Valid @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @Valid @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(value = "uris", defaultValue = "", required = false) List<String> uris,
                                           @RequestParam(value = "unique", defaultValue = "false", required = false) boolean unique) {

        log.info("GET request: start ={}, end={}, uris = {}, unique = {}", start, end, uris, unique);
        return client.getStats(start, end, uris, unique);
    }
}