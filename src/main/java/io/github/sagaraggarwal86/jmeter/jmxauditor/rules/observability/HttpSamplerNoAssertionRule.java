package io.github.sagaraggarwal86.jmeter.jmxauditor.rules.observability;

import io.github.sagaraggarwal86.jmeter.jmxauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jmxauditor.rules.AbstractRule;
import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.testelement.TestElement;

import java.util.Enumeration;
import java.util.List;
import java.util.Set;

public final class HttpSamplerNoAssertionRule extends AbstractRule {
    private static boolean hasAssertionIn(JMeterTreeNode node) {
        Enumeration<?> en = node.children();
        while (en.hasMoreElements()) {
            Object c = en.nextElement();
            if (c instanceof JMeterTreeNode tn && tn.getTestElement() instanceof Assertion) return true;
        }
        return false;
    }

    @Override
    public String id() {
        return "HTTP_SAMPLER_NO_ASSERTION";
    }

    @Override
    public Category category() {
        return Category.OBSERVABILITY;
    }

    @Override
    public Severity severity() {
        return Severity.WARN;
    }

    @Override
    public String description() {
        return "HTTP Sampler without a Response Assertion at element or parent scope.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(HTTPSamplerBase.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        if (hasAssertionIn(node)) return List.of();
        JMeterTreeNode parent = (JMeterTreeNode) node.getParent();
        while (parent != null) {
            if (hasAssertionIn(parent)) return List.of();
            parent = (JMeterTreeNode) parent.getParent();
        }
        return List.of(make(ctx.pathFor(node),
                "HTTP Sampler has no Response Assertion",
                "This HTTP sampler has no Response Assertion attached to it or inherited from any ancestor. JMeter's default definition of 'success' is just 'the connection completed and the HTTP status code was under 400' — so a 200 response containing an actual error page, an empty body, a captcha, or a maintenance message all count as passing samples. Error rate graphs stay green while the system under test is in fact completely broken.",
                "Add a Response Assertion as a child of the sampler, or on an ancestor (Thread Group, Transaction Controller) so it applies to multiple samplers at once. A minimal useful check asserts that the response code equals 200, or that the response text contains a string you expect on success ('Welcome', 'orderId', etc.). Even one such check makes the error rate trustworthy. For APIs, asserting on a JSON field via a JSON Assertion is usually stronger than a status-code-only check."));
    }
}
