package io.github.sagaraggarwal86.jmeter.jauditor.rules.scalability;

import io.github.sagaraggarwal86.jmeter.jauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jauditor.rules.AbstractRule;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.TestElement;

import java.util.List;
import java.util.Set;

public final class BeanshellUsageRule extends AbstractRule {
    @Override
    public String id() {
        return "BEANSHELL_USAGE";
    }

    @Override
    public Category category() {
        return Category.SCALABILITY;
    }

    @Override
    public Severity severity() {
        return Severity.WARN;
    }

    @Override
    public String description() {
        return "BeanShell Sampler/PreProcessor/PostProcessor/Assertion/Listener in use.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(TestElement.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        TestElement te = node.getTestElement();
        String cls = te.getClass().getName();
        if (!cls.contains("beanshell") && !cls.contains("BeanShell")) return List.of();
        return List.of(make(ctx.pathFor(node),
                "BeanShell element in use",
                "BeanShell is an older scripting engine that JMeter has officially deprecated. It's single-threaded internally, which means every BeanShell sampler or processor in the test plan becomes a bottleneck — threads have to queue up to execute the script one at a time, no matter how many CPU cores you have. On top of that, BeanShell is interpreted rather than compiled, so the raw per-call overhead is much higher than the modern alternatives.",
                "Swap this element for its JSR223 equivalent — JSR223 Sampler instead of BeanShell Sampler, JSR223 PreProcessor instead of BeanShell PreProcessor, and so on. In the JSR223 element, set the Language dropdown to 'groovy' (pre-installed with JMeter and much faster). The script syntax is almost identical to BeanShell, so most existing scripts copy across with minimal changes. Don't forget to set a Cache Key on each JSR223 element so Groovy compiles the script once instead of on every execution."));
    }
}
