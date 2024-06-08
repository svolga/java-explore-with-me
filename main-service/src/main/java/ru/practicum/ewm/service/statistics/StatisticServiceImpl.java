package ru.practicum.ewm.service.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.ewm.http.StatsClient;
import ru.practicum.ewm.utils.Constant;
import ru.practicum.ewm.utils.formatter.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticServiceImpl implements StatisticsService {

    private final StatsClient statsClient;
    @Value(value = "${app.name}")
    private String appName;

    @Override
    public void saveStats(HttpServletRequest request) {
        statsClient.saveRequestData(EndpointHit.builder()
                .app(appName)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.DATE_TIME_FORMATTER))
                .build());

    }

    @Override
    public Map<Long, Long> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) {

        ObjectMapper mapper = new ObjectMapper();
        List<ViewStats> stats;
        List<?> list;

        ResponseEntity<Object> response = statsClient.getStatistics(
                start.format(DateTimeFormatter.DATE_TIME_FORMATTER),
                end.format(DateTimeFormatter.DATE_TIME_FORMATTER),
                uris, true);

        if (!response.getStatusCode().is2xxSuccessful() || !response.hasBody()) {
            throw new HttpServerErrorException(response.getStatusCode(),
                    String.format("Ошибка получения статистики по url --> %s", uris));
        }

        if (response.getBody() instanceof List<?>) {
            list = (List<?>) response.getBody();
        } else {
            throw new ClassCastException("Данные с сервера статистики не могут быть извлечены");
        }
        if (list.isEmpty()) {
            return uris.stream().map(this::getEventIdFromUri)
                    .collect(Collectors.toMap(Function.identity(), s -> 0L));
        } else {
            stats = list.stream().map(e -> mapper.convertValue(e, ViewStats.class)).collect(Collectors.toList());
            log.info("Данные статистики --> {}", stats);
            return stats.stream()
                    .collect(Collectors.toMap(ViewStats -> getEventIdFromUri(ViewStats.getUri()),
                            ViewStats::getHits));
        }
    }

    private Long getEventIdFromUri(String uri) {
        String[] parts = uri.split(Constant.DELIM_URL);
        return Long.parseLong(parts[parts.length - 1]);
    }
}
