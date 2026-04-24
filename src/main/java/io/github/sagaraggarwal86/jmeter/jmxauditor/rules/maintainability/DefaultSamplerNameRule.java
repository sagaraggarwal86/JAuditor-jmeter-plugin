package io.github.sagaraggarwal86.jmeter.jmxauditor.rules.maintainability;

import io.github.sagaraggarwal86.jmeter.jmxauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jmxauditor.rules.AbstractRule;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;

import java.util.List;
import java.util.Set;

public final class DefaultSamplerNameRule extends AbstractRule {

    private static final Set<String> DEFAULTS = Set.of(
            "HTTP Request", "Debug Sampler", "JSR223 Sampler", "JDBC Request",
            "SOAP/XML-RPC Request", "FTP Request", "TCP Sampler", "JMS Publisher",
            "JMS Subscriber", "Java Request", "BeanShell Sampler"
    );

    @Override
    public String id() {
        return "DEFAULT_SAMPLER_NAME";
    }

    @Override
    public Category category() {
        return Category.MAINTAINABILITY;
    }

    @Override
    public Severity severity() {
        return Severity.INFO;
    }

    @Override
    public String description() {
        return "Sampler keeps JMeter's default name (hard to read in reports).";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(Sampler.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        String name = node.getName();
        if (name == null) return List.of();
        if (!DEFAULTS.contains(name.trim())) return List.of();
        return List.of(make(ctx.pathFor(node),
                "Sampler uses default name",
                "This sampler still carries JMeter's default name, '" + name + "'. In the results table, Aggregate Report, and every summary graph, that default name labels every measurement. When the test plan has several samplers all called 'HTTP Request' (or any other default), you can't tell which one is spiking, which one is slow, or which one is failing — the labels are indistinguishable.",
                "Rename the sampler to something that describes the business action it represents, ideally method plus endpoint or an operation name — for example, 'POST /checkout', 'GET product detail', or 'Login — fetch CSRF token'. The new name flows through automatically to every report and listener, so after the rename the metrics become immediately readable. A good test of a name: if a colleague sees it in a results table without context, can they tell what the request does?"));
    }
}
