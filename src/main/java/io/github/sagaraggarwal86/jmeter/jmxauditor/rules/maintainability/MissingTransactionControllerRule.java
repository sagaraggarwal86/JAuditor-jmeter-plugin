package io.github.sagaraggarwal86.jmeter.jmxauditor.rules.maintainability;

import io.github.sagaraggarwal86.jmeter.jmxauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jmxauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jmxauditor.rules.AbstractRule;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.ThreadGroup;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

public final class MissingTransactionControllerRule extends AbstractRule {
    @Override
    public String id() {
        return "MISSING_TRANSACTION_CONTROLLER";
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
        return "Thread Group has samplers not wrapped in Transaction Controllers.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(ThreadGroup.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        List<JMeterTreeNode> loose = new ArrayList<>();
        Enumeration<?> en = node.children();
        while (en.hasMoreElements()) {
            Object c = en.nextElement();
            if (c instanceof JMeterTreeNode tn) {
                TestElement te = tn.getTestElement();
                if (te instanceof Sampler) loose.add(tn);
            }
        }
        if (loose.isEmpty()) return List.of();
        // If user wraps samplers in Transaction Controllers elsewhere, direct-under-TG samplers are still unwrapped.
        // We still fire — the issue is the loose samplers, not absence of any TC anywhere.
        return List.of(make(ctx.pathFor(node),
                "Samplers outside Transaction Controllers",
                "This Thread Group has " + loose.size() + " sampler(s) as direct children with no Transaction Controller grouping them. Each sampler shows up as its own row in aggregate reports, which means the per-business-action view of the test has to be reconstructed by hand. A realistic flow like 'checkout' might touch six samplers (load cart, validate promo, submit payment, confirm, etc.); without a Transaction Controller you get six separate rows instead of one 'Checkout' row with a clean end-to-end duration.",
                "Group related samplers under a Transaction Controller named for the business flow they represent — 'Checkout', 'User Login', 'Search Product'. In reports, the controller shows up as a single row with its total duration (time from the first sampler starting to the last one finishing), alongside the individual sampler rows. Turn on 'Generate Parent Sample' on the controller if you want only the grouped row in the summary; leave it off if you want both the grouped row and the individual ones."));
    }
}
