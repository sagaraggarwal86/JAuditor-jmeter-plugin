# Changelog

All notable changes to JMXAuditor will be documented in this file.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [0.2.0] — 2026-04-24

### Changed (breaking)

- Plugin renamed from **JAuditor** to **JMXAuditor**. The prior `J` prefix read as "Java"; the new name binds the tool
  to JMeter's native `.jmx` format.
- Maven coordinates: `io.github.sagaraggarwal86:jauditor-jmeter-plugin` →
  `io.github.sagaraggarwal86:jmxauditor-jmeter-plugin`.
- JAR filename: `jauditor-jmeter-plugin-<ver>.jar` → `jmxauditor-jmeter-plugin-<ver>.jar`. Remove the old JAR from
  `<JMETER_HOME>/lib/ext/` before dropping the new one in.
- Default export filename prefix: `jauditor-report-*` → `jmxauditor-report-*` (HTML/JSON/XLSX).
- `jmeter.log` prefix: `JAuditor: …` → `JMXAuditor: …`. Update any log-filter regexes.
- Java package: `io.github.sagaraggarwal86.jmeter.jauditor.*` →
  `io.github.sagaraggarwal86.jmeter.jmxauditor.*`.
- JSON schema version unchanged (**1.0**). No JSON field names or enum values were affected.

## [0.1.0] — 2026-04-23

Initial release.

### Added

- 25 static-analysis rules across six categories (Correctness, Security, Scalability, Realism, Maintainability,
  Observability).
- GUI-mode plugin for JMeter 5.6.3 with three entry points: Tools menu, toolbar button, `Ctrl+Shift+A` shortcut.
- Modeless results dialog with KPI cards, severity tabs, sortable findings table, and click-to-navigate.
- HTML report export (single self-contained file, no external dependencies).
- JSON export (schema version 1.0).
- Session-only rule suppression via right-click context menu.
- Zero-impact contract: read-only scan, no network, no persistence, no `.jmx` modification.
