package io.github.sagaraggarwal86.jmeter.jmxauditor.rules.realism;

import io.github.sagaraggarwal86.jmeter.jmxauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jmxauditor.rules.AbstractRule;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.ThreadGroup;

import java.util.List;
import java.util.Set;

public final class MissingRampUpRule extends AbstractRule {
    @Override
    public String id() {
        return "MISSING_RAMP_UP";
    }

    @Override
    public Category category() {
        return Category.REALISM;
    }

    @Override
    public Severity severity() {
        return Severity.INFO;
    }

    @Override
    public String description() {
        return "Thread Group with >10 threads and zero ramp-up.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(ThreadGroup.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        TestElement te = node.getTestElement();
        int threads = propInt(te, "ThreadGroup.num_threads", 0);
        int ramp = propInt(te, "ThreadGroup.ramp_time", 0);
        if (threads <= 10 || ramp > 0) return List.of();
        return List.of(make(ctx.pathFor(node),
                "Thread Group has no ramp-up",
                "This Thread Group starts " + threads + " virtual users all at exactly the same instant (ramp-up period is 0 seconds). That's a traffic spike no real system ever sees — connection pools fill in a single millisecond, caches haven't warmed up, the JIT compiler hasn't finished optimising hot paths. The first few seconds of results reflect a cold, overwhelmed system rather than steady-state behaviour, which skews every averaged metric for the rest of the run.",
                "Set the Ramp-Up Period on the Thread Group to a non-zero value so JMeter introduces the threads gradually. A good rule of thumb is one to ten seconds per 100 threads — for example, 30-60 seconds for a 1000-thread group. Even for smaller runs, a 30-second ramp-up is usually enough to let connection pools, caches, and JIT compilation reach steady state before you start averaging the measurements that matter."));
    }
}
