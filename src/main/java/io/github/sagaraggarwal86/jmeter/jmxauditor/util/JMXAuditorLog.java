package io.github.sagaraggarwal86.jmeter.jmxauditor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SLF4J wrapper prefixing every message with {@code "JMXAuditor: "}.
 */
public final class JMXAuditorLog {

    private static final String PREFIX = "JMXAuditor: ";
    private static final String REDACTED = "****";

    private final Logger delegate;

    private JMXAuditorLog(Logger delegate) {
        this.delegate = delegate;
    }

    public static JMXAuditorLog forClass(Class<?> c) {
        return new JMXAuditorLog(LoggerFactory.getLogger(c));
    }

    /**
     * Returns {@code "****"}. Parameter is discarded so the value never reaches logs, findings, or reports.
     */
    public static String redact(String value) {
        return REDACTED;
    }

    public void info(String msg) {
        delegate.info(PREFIX + msg);
    }

    public void info(String fmt, Object... args) {
        delegate.info(PREFIX + fmt, args);
    }

    public void debug(String msg) {
        delegate.debug(PREFIX + msg);
    }

    public void debug(String fmt, Object... args) {
        delegate.debug(PREFIX + fmt, args);
    }

    public void warn(String msg) {
        delegate.warn(PREFIX + msg);
    }

    public void warn(String msg, Throwable t) {
        delegate.warn(PREFIX + msg, t);
    }

    public void warn(String fmt, Object... args) {
        delegate.warn(PREFIX + fmt, args);
    }

    public void error(String msg) {
        delegate.error(PREFIX + msg);
    }

    public void error(String msg, Throwable t) {
        delegate.error(PREFIX + msg, t);
    }

    public void logScanSummary(int findings, int nodes, long durationMs, String outcome) {
        delegate.info(PREFIX + "scan complete: {} findings, {} nodes, {} ms, outcome={}",
                findings, nodes, durationMs, outcome);
    }
}
