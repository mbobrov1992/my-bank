package ru.yandex.practicum.my.bank.front.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtils {

    public static Mono<Void> putSessionKey(ServerWebExchange exchange, String key, Object value) {
        return exchange.getSession()
                .doOnNext(session -> session.getAttributes().put(key, value))
                .then();
    }

    public static Mono<String> getSessionKey(ServerWebExchange exchange, String key) {
        return exchange.getSession()
                .mapNotNull(session -> {
                    String value = session.getAttribute(key);
                    if (value != null) {
                        session.getAttributes().remove(key);
                    }
                    return value;
                });
    }
}
