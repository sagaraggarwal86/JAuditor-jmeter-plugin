package io.github.sagaraggarwal86.jmeter.jmxauditor.util;

import java.time.Instant;

public interface Clock {
    static Clock system() {
        return Instant::now;
    }

    Instant now();
}
