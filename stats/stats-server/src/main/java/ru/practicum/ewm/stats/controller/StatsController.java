package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.ewm.stats.service.StatsService;
import ru.practicum.ewm.stats.utils.Constant;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping("hit")
    public EndpointHit addHit(@Valid @RequestBody EndpointHit endpointHit) {
        log.info("POST request: endpointHit = {}", endpointHit);
        return statsService.create(endpointHit);
    }

    @GetMapping("stats")
    public List<ViewStats> getStats(
            @Valid @RequestParam("start") @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT) LocalDateTime start,
            @Valid @RequestParam("end") @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT) LocalDateTime end,
            @RequestParam(value = "uris", defaultValue = "", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false", required = false) boolean unique
    ) {
        log.info("GET request: start ={}, end={}, uris = {}, unique = {}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }
}