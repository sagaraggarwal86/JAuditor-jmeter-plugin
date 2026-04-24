package io.github.sagaraggarwal86.jmeter.jauditor.model;

import io.github.sagaraggarwal86.jmeter.jauditor.engine.ScanLimits;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModelTest {

    @Test
    void nodePathBreadcrumbUsesChevron() {
        var p = new NodePath(List.of("Test Plan", "Thread Group", "HTTP Request"));
        assertThat(p.breadcrumb()).isEqualTo("Test Plan › Thread Group › HTTP Request");
        assertThat(p.depth()).isEqualTo(3);
    }

    @Test
    void findingRejectsNullRuleId() {
        assertThatThrownBy(() -> new Finding(null, Category.CORRECTNESS, Severity.ERROR, "t", "d", "s",
                new NodePath(List.of("x")), 1))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void scanOutcomeBanner() {
        assertThat(ScanOutcome.COMPLETE.isTruncated()).isFalse();
        assertThat(ScanOutcome.NODE_LIMIT.bannerMessage(0))
                .contains(String.valueOf(ScanLimits.MAX_NODES));
        assertThat(ScanOutcome.TIMEOUT.isTruncated()).isTrue();
        assertThat(ScanOutcome.FINDING_LIMIT.bannerMessage(0))
                .contains(String.valueOf(ScanLimits.MAX_FINDINGS));
    }

    @Test
    void severityAndCategoryJsonValuesLowercase() {
        assertThat(Severity.ERROR.asJson()).isEqualTo("error");
        assertThat(Category.MAINTAINABILITY.asJson()).isEqualTo("maintainability");
    }
}
