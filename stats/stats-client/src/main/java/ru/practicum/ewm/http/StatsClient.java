package ru.practicum.ewm.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHit;
import ru.practicum.ewm.util.UrlQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
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

    public ResponseEntity<Object> create(EndpointHit endpointHit) {
        return post(LINK_HIT, endpointHit);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> params = Map.of(
                PARAMETER_START, start,
                PARAMETER_END, end,
                PARAMETER_URIS, uris,
                PARAMETER_UNIQUE, unique
        );

        String urlQquery = LINK_STATS + "?" + UrlQuery.urlEncode(params);
        return get(urlQquery, params);
    }

}