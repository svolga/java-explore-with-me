package ru.practicum.ewm.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHit;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/";
    private static final String LINK_HIT = "hit";
    private static final String LINK_STATS = "stats";
    private static final String PARAMETER_START = "start";
    private static final String PARAMETER_END = "end";
    private static final String PARAMETER_URIS = "uris";
    private static final String PARAMETER_UNIQUE = "unique";

    @Autowired
    public StatsClient(@Value("${stats.server.url}") String url, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(url + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> saveRequestData(EndpointHit hit) {
        ResponseEntity<Object> response = post(LINK_HIT, hit);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response.getBody());
        }
        return response;
    }

    public ResponseEntity<Object> getStatistics(String start, String end, List<String> uris, Boolean unique) {

        Map<String, Object> params = Map.of(
                PARAMETER_START, start,
                PARAMETER_END, end,
                PARAMETER_UNIQUE, unique
        );

        log.info("GET запрос клиента: {}, {}, {}, {}:", start, end, uris, unique);
        return get(LINK_STATS + getUrlParams(start, end, uris, unique), params);
    }

    private String getUrlParams(String start, String end, List<String> listUrl, Boolean unique) {

        String urls = String.join(",", listUrl);
        StringBuilder sb = new StringBuilder("?");
        return sb.append(getKeyValueUrl(PARAMETER_START, start))
                .append("&")
                .append(getKeyValueUrl(PARAMETER_END, end))
                .append("&")
                .append(getKeyValueUrl(PARAMETER_URIS, urls))
                .append("&")
                .append(getKeyValueUrl(PARAMETER_UNIQUE, unique))
                .toString();
    }

    private String getKeyValueUrl(String key, Object value) {
        return key + "=" + value;
    }

}