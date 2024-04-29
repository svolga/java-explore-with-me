package ru.practicum.ewm.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlQuery {

    public static String urlEncode(Map<?, ?> map) {
        return map.entrySet().stream().map(
                entry -> entry.getValue() == null
                        ? urlEncode(entry.getKey())
                        : urlEncode(entry.getKey()) + "=" + urlEncode(entry.getValue())
        ).collect(Collectors.joining("&"));
    }

    public static String urlEncode(Object obj) {
        return URLEncoder.encode(obj.toString(), StandardCharsets.UTF_8);
    }

}
