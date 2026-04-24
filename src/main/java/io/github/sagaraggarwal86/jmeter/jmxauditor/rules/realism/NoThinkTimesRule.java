package io.github.sagaraggarwal86.jmeter.jmxauditor.rules.realism;

import io.github.sagaraggarwal86.jmeter.jmxauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jmxauditor.rules.AbstractRule;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.Timer;

import java.util.List;
import java.util.Set;

public final class NoThinkTimesRule extends AbstractRule {
    @Override
    public String id() {
        return "NO_THINK_TIMES";
    }

    @Override
    public Category category() {
        return Category.REALISM;
    }

    @Override
    public Severity severity() {
        return Severity.WARN;
    }

    @Override
    public String description() {
        return "Thread Group contains samplers but no Timer in its subtree.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(ThreadGroup.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        if (!ctx.hasDescendantOfType(node, Sampler.class)) return List.of();
        if (ctx.hasDescendantOfType(node, Timer.class)) return List.of();
        return List.of(make(ctx.pathFor(node),
                "Thread Group has no think times",
                "This Thread Group runs its requests one right after another with nothing slowing them down. Real users pause between actions — they read the page, scroll, type, decide — so a load test without pauses hits the server much faster and harder than production traffic ever would, and the response times and error rates you get back won't reflect what real users experience.",
                "Add a Timer element somewhere inside this Thread Group so JMeter pauses between requests. A Constant Timer gives every thread the same fixed delay (e.g., 2 seconds) — quick to set up. A Gaussian Random Timer varies the delay around a target average ('about 3 seconds, give or take one') — more realistic. Even a few seconds of pause per action usually makes the load shape look much closer to real traffic."));
    }
}
