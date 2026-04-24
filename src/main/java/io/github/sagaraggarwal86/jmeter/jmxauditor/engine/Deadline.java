package io.github.sagaraggarwal86.jmeter.jmxauditor.engine;

import io.github.sagaraggarwal86.jmeter.jmxauditor.util.Clock;

import java.time.Duration;
import java.time.Instant;

/**
 * Wall-clock expiration check driven by an injected {@link Clock}.
 */
public final class Deadline {
    private final Clock clock;
    private final Instant at;

    public Deadline(Clock clock, Duration budget) {
        this.clock = clock;
        this.at = clock.now().plus(budget);
    }

    public boolean expired() {
        return !clock.now().isBefore(at);
    }
}
